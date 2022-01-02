package atmMVC.controller;

import atmMVC.controller.CustomerCommonComponents;
import atmMVC.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A customer accounts overview controller for the UI.
 */
public class CustomerAccountsOverviewController extends CustomerCommonComponents {
    @FXML
    private TableView<AccountForFX> accountTable;
    @FXML
    private TableColumn<AccountForFX, Integer> accountNumberColumn;
    @FXML
    private TableColumn<AccountForFX, String> accountTypeColumn;
    @FXML
    private TableColumn<AccountForFX, BigDecimal> balanceColumn;
    @FXML
    private TableColumn<AccountForFX, LocalDateTime> accountOpenDateColumn;
    @FXML
    private TableColumn<AccountForFX, Boolean> primaryColumn;
    @FXML
    private TableColumn<AccountForFX, Boolean> jointColumn;

    @FXML
    private Label netTotalLabel;
    @FXML
    private Label mostRecentPromptLabel;
    @FXML
    private Label secondNumberPromptLabel;
    @FXML
    private Label trxIdLabel;
    @FXML
    private Label trxTypeLabel;
    @FXML
    private Label trxAmountLabel;
    @FXML
    private Label trxDateTimeLabel;
    @FXML
    private Label secondNumberLabel;
    @FXML
    private Label undoableLabel;
    @FXML
    private Label isReversingEntryLabel;

    private ObservableList<AccountForFX> accounts = FXCollections.observableArrayList();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public CustomerAccountsOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the table with the columns.
        accountNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAccountNumber()).asObject());
        accountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());

        balanceColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<BigDecimal>(cellData.getValue().getBalance()));

        accountOpenDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getAccountOpenDate()));

        primaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
        jointColumn.setCellValueFactory(cellData -> cellData.getValue().jointProperty());

        // Listen for selection changes and show the person details when changed.
        accountTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMostRecentTrxDetails(newValue));
    }

    public void setAccountTable() {
        accounts = this.customer.getAccountsFX();
        accountTable.setItems(accounts);
        netTotalLabel.setText(this.customer.getTotalBalance().toString());
    }

    private void showMostRecentTrxDetails(AccountForFX accountForFX){
        Account account = this.customer.findAccount(accountForFX.getAccountNumber());
        Transaction transaction = BankPerson.findTransaction(account.getRecentTrxID());
        if(transaction != null) {
            mostRecentPromptLabel.setText("Most Recent Transaction: ");
            trxIdLabel.setText(Integer.toString(transaction.getTrxID()));
            trxTypeLabel.setText(transaction.getTrxType());
            trxAmountLabel.setText(transaction.getTrxAmount().toString());
            trxDateTimeLabel.setText(transaction.getTrxDateTime().toString());
            if(transaction.getSecondID() == 0)
                secondNumberLabel.setText("");
            else
                secondNumberLabel.setText(Integer.toString(transaction.getSecondID()));
            undoableLabel.setText(Boolean.toString(transaction.getUndoable()));
            isReversingEntryLabel.setText(Boolean.toString(transaction.getIsReversingEntry()));
            setSecondNumberPrompt(transaction);
        }
        else {
            mostRecentPromptLabel.setText("No transaction for this account.");
            resetMostRecentTrxDetails();
        }
    }
    private void setSecondNumberPrompt(Transaction trx) {
        if (trx instanceof TransferIn)
            secondNumberPromptLabel.setText("From Account: ");
        else if (trx instanceof TransferOut)
            secondNumberPromptLabel.setText("To Account: ");
        else if (trx instanceof TransferToOther)
            secondNumberPromptLabel.setText("To Customer: ");
        else if (trx instanceof PayBill)
            secondNumberPromptLabel.setText("Payee Number: ");
        else
            secondNumberPromptLabel.setText("");

    }

    private void resetMostRecentTrxDetails() {
        trxIdLabel.setText("");
        trxTypeLabel.setText("");
        trxAmountLabel.setText("");
        trxDateTimeLabel.setText("");
        secondNumberLabel.setText("");
        undoableLabel.setText("");
        isReversingEntryLabel.setText("");
    }

    public void setFirstEntry() {
        if(accountTable.getItems().size() > 0) {
            accountTable.getSelectionModel().selectFirst();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
