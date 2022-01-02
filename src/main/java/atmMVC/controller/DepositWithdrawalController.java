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
 * A controller that handles deposits and withdrawals in the UI.
 */
public class DepositWithdrawalController extends CustomerCommonComponents {
    @FXML
    private TextField amountField;
    @FXML
    private TextField currencyField;
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
    private Label amountPromptLabel;
    @FXML
    private Label accountPromptLabel;
    @FXML
    private Label currencyPromptLabel;
    @FXML
    private Button doButton;

    private ObservableList<AccountForFX> accounts = FXCollections.observableArrayList();
    private AccountForFX selectedAccount;
    private boolean isDeposit;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public DepositWithdrawalController() {
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
                new SimpleObjectProperty<>(cellData.getValue().getBalance()));

        accountOpenDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getAccountOpenDate()));

        primaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
        jointColumn.setCellValueFactory(cellData -> cellData.getValue().jointProperty());
    }

    void setAccountTable() {
        accounts = this.customer.getAccountsFX();
        accountTable.setItems(accounts);
    }

    void setControlNames(boolean isDeposit){
        this.isDeposit = isDeposit;
        if(isDeposit) {
            amountPromptLabel.setText("How much do you want to deposit? (number only)  ");
            accountPromptLabel.setText("Select account below to deposit to:");
            currencyPromptLabel.setText("Select currency you want to deposit (CAD, USD)");
            doButton.setText("Deposit");
        }
        else {
            amountPromptLabel.setText("How much do you want to withdraw? (multiple of 5)");
            accountPromptLabel.setText("Select account below to withdraw from:");
            currencyPromptLabel.setText("Select currency you want to withdraw (CAD, USD)");
            doButton.setText("Withdraw");
        }
    }


    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDepositWithdraw() {
        if (isInputValid()) {
            doDepositWithdrawal(this.isDeposit);
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

        if (!currencyField.getText().equals("CAD")  && !currencyField.getText().equals("USD")) {
            errorMessage += "Currency choice must match given codes.";
        }

        if(accountTable.getSelectionModel().getSelectedIndex() <0 ) {
            errorMessage += "Please select an account in the table.\n";
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

    private void doDepositWithdrawal(boolean isDeposit) {
        selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        boolean result = false;
        Account account = this.customer.findAccount(selectedAccount.getAccountNumber());
        BigDecimal serviceFee = new BigDecimal(0.98);

        if (isDeposit) { //deposit
            String depositCurrency = currencyField.getText();
            if (depositCurrency.equals("CAD")) {
                Deposit deposit = new Deposit(
                        account, new BigDecimal(amountField.getText()), LocalDateTime.now());
                result = deposit.doTransaction();
            } else { //depositing foreign currency
                double exchangeRate = ExchangeConverter.ExchangeRate(depositCurrency,"CAD");
                BigDecimal amountCAD = new BigDecimal(amountField.getText()).multiply(BigDecimal.valueOf(exchangeRate));
                //service fee of 2%
                amountCAD = amountCAD.multiply(serviceFee);
                Deposit deposit = new Deposit(account, new BigDecimal(amountField.getText()), amountCAD,
                        LocalDateTime.now(), depositCurrency);
                result = deposit.doTransaction();
            }

        } else { //withdrawal
            String withdrawCurrency = currencyField.getText();
            if (withdrawCurrency.equals("CAD")) {
                Withdrawal withdraw = new Withdrawal(
                        account, new BigDecimal(amountField.getText()), LocalDateTime.now());
                if (isWithdrawable(withdraw))
                    result = withdraw.doTransaction();
            } else { //withdrawing foreign currency
                double exchangeRate = ExchangeConverter.ExchangeRate(withdrawCurrency, "CAD");
                BigDecimal amountCAD = new BigDecimal(amountField.getText()).multiply(BigDecimal.valueOf(exchangeRate));
                //service fee of 2%
                amountCAD = amountCAD.multiply(serviceFee);
                Withdrawal withdraw = new Withdrawal(account, amountCAD,
                        new BigDecimal(amountField.getText()), LocalDateTime.now(), withdrawCurrency);
                if (isWithdrawable(withdraw))
                    result = withdraw.doTransaction();
            }
        }
        showResult(result, account, currencyField.getText());
    }

    private void showResult(boolean successful, Account account, String currency) {
        if (successful) {
            selectedAccount.setBalance(account.getBalance());
            accountTable.refresh();
            String message;
            if(this.isDeposit)
                message = " has been deposited to your ";
            else
                message = " has been Withdrawn from your ";
            showAlert("ATM MVC", "Successful",
                    "$" + amountField.getText() + " " + currency + message +
                            selectedAccount.accountTypeProperty().getValueSafe() + " " +
                            selectedAccount.getAccountNumber(), Alert.AlertType.INFORMATION);
            amountField.setText("");
            accountTable.getSelectionModel().clearSelection();
        }
        else
            showAlert("ATM MVC", "Failed",
                    "Request Denied", Alert.AlertType.ERROR);

    }

    private boolean isWithdrawable(Withdrawal withdraw) {
        String errorMessage = "";

        if (withdraw.getCurrency() == null && !withdraw.isMultipleOf5(withdraw.getTrxAmount())) {
            errorMessage += "The amount is not the multiple of $5!\n";
        }
        else if (!withdraw.isMultipleOf5(withdraw.getAmountForeign()) && withdraw.getCurrency().equals("USD")) {
            errorMessage += "The amount is not the multiple of $5!\n";
        }
        if (withdraw.getCurrency() == null && !withdraw.atmHasEnoughMoney(withdraw.getTrxAmount())) {
            errorMessage += "Withdraw amount is greater than ATM has!\n";
        }
        else if (!withdraw.atmHasEnoughMoney(withdraw.getAmountForeign()) && withdraw.getCurrency().equals("USD")) {
            errorMessage += "Withdraw amount is greater than ATM has!\n";
        }
        if(!withdraw.getAccount().isAbleToTakeOutMoney(withdraw.getTrxAmount())) {
            errorMessage += "This exceeds the Account's allowed amount!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "Please correct following error(s)",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }
}
