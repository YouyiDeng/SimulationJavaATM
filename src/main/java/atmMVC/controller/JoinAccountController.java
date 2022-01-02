package atmMVC.controller;

import atmMVC.controller.BankCommonComponents;
import atmMVC.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * A controller that handles joint accounts in the UI.
 */
public class JoinAccountController extends BankCommonComponents {
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
    private TextField secondNumberField;


    private ObservableList<AccountForFX> accounts = FXCollections.observableArrayList();
    private AccountForFX selectedAccount;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public JoinAccountController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the able with the columns.
        accountNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAccountNumber()).asObject());
        accountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());

        balanceColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<BigDecimal>(cellData.getValue().getBalance()));

        accountOpenDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getAccountOpenDate()));

        primaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
        jointColumn.setCellValueFactory(cellData -> cellData.getValue().jointProperty());
    }

    public void setAccountTable() {
        accounts = this.mainApp.getBank().getAllAccountsFX();
        Comparator<AccountForFX> comparator = Comparator.comparingInt(AccountForFX::getAccountNumber);
        FXCollections.sort(accounts, comparator);
        accountTable.setItems(accounts);
    }

    /**
     * Called when the user clicks make a join.
     */
    @FXML
    private void handleMakJoint() {
        if(isInputValid())
            makeJoin();
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

        if(accountTable.getSelectionModel().getSelectedIndex() < 0 ) {
            errorMessage += "Please select an account in the table.\n";
        }

        if(!isNumeric(secondNumberField.getText())) {
                errorMessage += "Customer number has to be a number.\n";
        }
        else {
            Customer customer = Bank.findCustomer(Integer.parseInt(secondNumberField.getText()));
            Account account = null;
            if(selectedAccount != null)
                account= Bank.findAccount(selectedAccount.getAccountNumber());
            if(customer == null)
                    errorMessage += "The customer " + secondNumberField.getText() + " doesn't exist.\n";
            else {
                if(account != null && customer.getCustomerNumber() == account.getCustomerNumber())
                    errorMessage = "Cannot join to same customer";
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

    private void makeJoin() {
        selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        boolean result;
        result = this.bankPerson.makeAccountJoint(selectedAccount.getAccountNumber(),
                Integer.parseInt(secondNumberField.getText().trim()));

        showResult(result, selectedAccount);
    }

    private void showResult(boolean successful, AccountForFX accountForFX) {
        String message;
        if (successful) {
            secondNumberField.setText("");
            accountForFX.setJoint(true);
            accountTable.refresh();
            message = "Account " + accountForFX.getAccountNumber() + " has been joint";
            showAlert("ATM MVC", "Successful",
                    message, Alert.AlertType.INFORMATION);
        }
        else {
            message = "Cannot process join.  Reason may be:\n" +
                    "This account is already joint\n" +
                    "Only ChequingAccount and SavingAccount can be joint account";
            showAlert("ATM MVC", "Failed",
                    message, Alert.AlertType.ERROR);
        }
    }
}
