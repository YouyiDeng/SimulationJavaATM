package atmMVC.controller;

import atmMVC.controller.BankCommonComponents;
import atmMVC.model.BankManager;
import atmMVC.model.Transaction;
import atmMVC.model.TransactionForFX;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A Controller that handles undoing transactions in the UI.
 */
public class UndoTransactionController extends BankCommonComponents {

    @FXML
    private TableView<TransactionForFX> trxTable;
    @FXML
    private TableColumn<TransactionForFX, Integer> trxIDColumn;
    @FXML
    private TableColumn<TransactionForFX, Integer> customerNumberColumn;
    @FXML
    private TableColumn<TransactionForFX, Integer> accountNumberColumn;
    @FXML
    private TableColumn<TransactionForFX, BigDecimal> trxAmountColumn;
    @FXML
    private TableColumn<TransactionForFX, String> trxTypeColumn;
    @FXML
    private TableColumn<TransactionForFX, LocalDateTime> trxDateTimeColumn;
    @FXML
    private TableColumn<TransactionForFX, Integer> secondNumberColumn;
    @FXML
    private TableColumn<TransactionForFX, Boolean> undoableColumn;
    @FXML
    private TableColumn<TransactionForFX, Boolean> isReversingEntryColumn;

    private ObservableList<TransactionForFX> transactions = FXCollections.observableArrayList();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public UndoTransactionController() { }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the table with the columns.
        trxIDColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getTrxID()).asObject());
        customerNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCustomerNumber()).asObject());
        accountNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAccountNumber()).asObject());
        trxAmountColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<BigDecimal>(cellData.getValue().getTrxAmount()));
        trxTypeColumn.setCellValueFactory(cellData -> cellData.getValue().trxTypeProperty());

        trxDateTimeColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getTrxDateTime()));
        secondNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSecondNumber()).asObject());

        undoableColumn.setCellValueFactory(cellData -> cellData.getValue().undoableProperty());
        isReversingEntryColumn.setCellValueFactory(cellData -> cellData.getValue().isReversingEntryProperty());
    }

    public void setTrxTable() {
        transactions = ((BankManager)this.bankPerson).getAllTransactions();
        trxTable.setItems(transactions);
    }


    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleUndoTransaction() {
        if (isInputValid()) {
            int unDoTrxID = trxTable.getSelectionModel().getSelectedItem().getTrxID();
            boolean undoResult = ((BankManager)this.bankPerson).undoTransaction(unDoTrxID);
            if(undoResult) {
                Transaction mostRecentTrx = Transaction.getMostRecentTrx();
                TransactionForFX newTrxForFX = transactionForFXFromTrx(mostRecentTrx);
                transactions.add(newTrxForFX);

                showAlert("ATM MVC", "Transaction Is Undone",
                        "Transaction (" + unDoTrxID + ") has been undone", Alert.AlertType.INFORMATION);
            }
            else
                showAlert("ATM MVC", "No Transaction Undone",
                        "Transaction (" + unDoTrxID + ") cannot be undone", Alert.AlertType.ERROR);
        }
    }

    private TransactionForFX transactionForFXFromTrx(Transaction trx) {
        int trxID = trx.getTrxID();
        int customerNumber = trx.getAccount().getCustomerNumber();
        int accountNumber = trx.getAccount().getAccountNumber();
        BigDecimal trxAmount = trx.getTrxAmount();
        String trxType = trx.getTrxType();
        LocalDateTime trxDateTime = trx.getTrxDateTime();
        int secondNumber = trx.getSecondID();
        boolean undoable = trx.getUndoable();
        boolean isReversingEntry = trx.getIsReversingEntry();

        return new TransactionForFX(trxID,customerNumber, accountNumber, trxAmount,
                trxType, trxDateTime, secondNumber, undoable, isReversingEntry);
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user select a transaction.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if(trxTable.getSelectionModel().getSelectedIndex() <0 ) {
            errorMessage += "Please select a transaction in the table.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "No Transaction Selected",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }
}
