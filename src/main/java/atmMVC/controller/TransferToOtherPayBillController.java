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
 * A controller that handles transfers to other and paying bills in the UI.
 */
public class TransferToOtherPayBillController extends CustomerCommonComponents {
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
    private Label fromAccountPromptLabel;
    @FXML
    private Label toPromptLabel;

    @FXML
    private TextField secondNumberField;

    @FXML
    private Button doButton;

    private ObservableList<AccountForFX> fromAccounts = FXCollections.observableArrayList();
    private AccountForFX selectedFromAccount;
    private boolean isTransferToOther;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TransferToOtherPayBillController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
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

    public void setAccountTable() {
        fromAccounts = this.customer.getAccountsFX();
        fromAccountTable.setItems(fromAccounts);
    }

    public void setControlNames(boolean isTransferToOther){
        this.isTransferToOther = isTransferToOther;
        if(isTransferToOther) {
            fromAccountPromptLabel.setText("Select account below to transfer from:");
            toPromptLabel.setText("Which customer to transfer to? (number only)");
            doButton.setText("Transfer Out Other");
        }
        else {
            fromAccountPromptLabel.setText("Select account below to pay bill from:");
            toPromptLabel.setText("Which payee to pay bill for? (number only)");
            doButton.setText("Pay Bill");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleTransferOutOtherPayBill() {
        if (isInputValid()) {
            doTransferOutOtherPayBill(this.isTransferToOther);
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
            errorMessage += "Amount field is blank or not number!\n";
        }

        if(fromAccountTable.getSelectionModel().getSelectedIndex() < 0 ) {
            errorMessage += "Please select an account in the transfer from table.\n";
        }

        if(!isNumeric(secondNumberField.getText())) {
            if(this.isTransferToOther)
                errorMessage += "Customer number has to be number.\n";
            else
                errorMessage += "Payee number has to be number.\n";
        }
        else {
            if(this.isTransferToOther) {
                Customer toCustomer = Bank.findCustomer(Integer.parseInt(secondNumberField.getText()));
                if(toCustomer == null)
                    errorMessage += "The customer to transfer money to doesn't exist.\n";
            }
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

    private void doTransferOutOtherPayBill(boolean isTransferToOther) {
        selectedFromAccount = fromAccountTable.getSelectionModel().getSelectedItem();
        boolean result;
        Account fromAccount = this.customer.findAccount(selectedFromAccount.getAccountNumber());

        if (isTransferToOther && canTransferOutMoney(fromAccount, amountField)) {
            Customer toCustomer = Bank.findCustomer(Integer.parseInt(secondNumberField.getText()));
            TransferToOther transferToOther = new TransferToOther(fromAccount, new BigDecimal(amountField.getText()),
                        LocalDateTime.now(), toCustomer);
            result =  transferToOther.doTransaction();
        } else {
            PayBill payBill = new PayBill(fromAccount, new BigDecimal(amountField.getText()), LocalDateTime.now(),
                    Integer.parseInt(secondNumberField.getText()));
            result = payBill.doTransaction();
        }
        showResult(result, fromAccount);
    }

    private void showResult(boolean successful, Account fromAccount) {
        if (successful) {
            selectedFromAccount.setBalance(fromAccount.getBalance());
            fromAccountTable.refresh();
            if(isTransferToOther) {
                showAlert("ATM MVC", "Successful", "$" +
                        amountField.getText() + " has been transferred out to customer " +
                        secondNumberField.getText() + " from your " +
                        selectedFromAccount.accountTypeProperty().getValueSafe() + " " +
                        selectedFromAccount.getAccountNumber(), Alert.AlertType.INFORMATION);

            }
            else {
                showAlert("ATM MVC", "Successful", "$" +
                        amountField.getText() + " has been paid to payee " +
                        secondNumberField.getText() + " from your " +
                        selectedFromAccount.accountTypeProperty().getValueSafe() + " " +
                        selectedFromAccount.getAccountNumber(), Alert.AlertType.INFORMATION);
            }
            amountField.setText("");
            fromAccountTable.getSelectionModel().clearSelection();
            secondNumberField.setText("");
        }
        else
            showAlert("ATM MVC", "Failed",
                    "Request Denied", Alert.AlertType.ERROR);
    }
}
