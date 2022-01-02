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
 * A controller that handles transfers in the UI.
 */
public class TransferController extends CustomerCommonComponents {
    @FXML
    private TextField amountField;

    @FXML
    private TableView<AccountForFX> fromAccountTable;
    @FXML
    private TableColumn<AccountForFX, Integer> fromAccountNumberColumn;
    @FXML
    private TableColumn<AccountForFX, String> fromAccountTypeColumn;
    @FXML
    private TableColumn<AccountForFX, BigDecimal> fromBalanceColumn;
    @FXML
    private TableColumn<AccountForFX, LocalDateTime> fromAccountOpenDateColumn;
    @FXML
    private TableColumn<AccountForFX, Boolean> fromPrimaryColumn;
    @FXML
    private TableColumn<AccountForFX, Boolean> fromJointColumn;

    @FXML
    private TableView<AccountForFX> toAccountTable;
    @FXML
    private TableColumn<AccountForFX, Integer> toAccountNumberColumn;
    @FXML
    private TableColumn<AccountForFX, String> toAccountTypeColumn;
    @FXML
    private TableColumn<AccountForFX, BigDecimal> toBalanceColumn;
    @FXML
    private TableColumn<AccountForFX, LocalDateTime> toAccountOpenDateColumn;
    @FXML
    private TableColumn<AccountForFX, Boolean> toPrimaryColumn;
    @FXML
    private TableColumn<AccountForFX, Boolean> toJointColumn;

    @FXML
    private Button doTransfer;

    private ObservableList<AccountForFX> fromAccounts = FXCollections.observableArrayList();
    private ObservableList<AccountForFX> toAccounts = FXCollections.observableArrayList();
    private AccountForFX selectedFromAccount;
    private AccountForFX selectedToAccount;
    private boolean isTransferIn;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TransferController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        initializeFromTable();
        initializeToTable();
    }
    private void initializeFromTable() {
        // Initialize the able with the columns.
        fromAccountNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAccountNumber()).asObject());
        fromAccountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());

        fromBalanceColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<BigDecimal>(cellData.getValue().getBalance()));

        fromAccountOpenDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getAccountOpenDate()));

        fromPrimaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
        fromJointColumn.setCellValueFactory(cellData -> cellData.getValue().jointProperty());
    }

    private void initializeToTable() {
        // Initialize the able with the columns.
        toAccountNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAccountNumber()).asObject());
        toAccountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());

        toBalanceColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<BigDecimal>(cellData.getValue().getBalance()));

        toAccountOpenDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getAccountOpenDate()));

        toPrimaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
        toJointColumn.setCellValueFactory(cellData -> cellData.getValue().jointProperty());
    }

    public void setAccountTables() {
        fromAccounts = this.customer.getAccountsFX();
        fromAccountTable.setItems(fromAccounts);

        toAccounts = this.customer.getAccountsFX();
        toAccountTable.setItems(toAccounts);
    }

    public void setControlNames(boolean isTransferIn){
        this.isTransferIn = isTransferIn;
        if(isTransferIn) {
            doTransfer.setText("Transfer In");
        }
        else {
            doTransfer.setText("Transfer Out");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleTransfer() {
        if (isInputValid() && notSameAccount()) {
            doTransfer(this.isTransferIn);
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * Validates the user select an account
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (!isDouble(amountField.getText())) {
            errorMessage += "Amount field is blank or not a number!\n";
        }

        if(fromAccountTable.getSelectionModel().getSelectedIndex() < 0 ) {
            errorMessage += "Please select an account in the transfer from table.\n";
        }

        if(toAccountTable.getSelectionModel().getSelectedIndex() < 0 ) {
            errorMessage += "Please select an account in the transfer to table.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "Please correct invalid fields",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }
    private boolean notSameAccount() {
        String errorMessage = "";
        if(fromAccountTable.getSelectionModel().getSelectedItem().getAccountNumber() ==
                toAccountTable.getSelectionModel().getSelectedItem().getAccountNumber()) {
            errorMessage += "Please select different accounts.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "Please correct",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }

    private void doTransfer(boolean isTransferIn) {
        selectedFromAccount = fromAccountTable.getSelectionModel().getSelectedItem();
        selectedToAccount = toAccountTable.getSelectionModel().getSelectedItem();
        boolean result = false;
        Account fromAccount = this.customer.findAccount(selectedFromAccount.getAccountNumber());
        Account toAccount = this.customer.findAccount(selectedToAccount.getAccountNumber());

        if (isTransferIn) {
            TransferIn transferIn = new TransferIn(toAccount,
                    new BigDecimal(amountField.getText()), LocalDateTime.now(), fromAccount);
            if(canTransferOutMoney(fromAccount, amountField))
                result = transferIn.doTransaction();
        } else {
            TransferOut transferOut = new TransferOut(fromAccount,
                    new BigDecimal(amountField.getText()), LocalDateTime.now(), toAccount);
            if(canTransferOutMoney(fromAccount, amountField))
                result = transferOut.doTransaction();
        }
        showResult(result, fromAccount, toAccount);
    }

    private void showResult(boolean successful, Account fromAccount, Account toAccount) {
        if (successful) {
            setAccountTables();

            showAlert("ATM MVC", "Successful", "$" +
                    amountField.getText() + " has been transferred into your " +
                    selectedToAccount.accountTypeProperty().getValueSafe() + " " +
                    selectedToAccount.getAccountNumber() + " from your " +
                    selectedFromAccount.accountTypeProperty().getValueSafe() + " " +
                    selectedFromAccount.getAccountNumber(), Alert.AlertType.INFORMATION);


            amountField.setText("");
            fromAccountTable.getSelectionModel().clearSelection();
            toAccountTable.getSelectionModel().clearSelection();
        }
        else
            showAlert("ATM MVC", "Failed",
                    "Request Denied", Alert.AlertType.ERROR);

    }
}
