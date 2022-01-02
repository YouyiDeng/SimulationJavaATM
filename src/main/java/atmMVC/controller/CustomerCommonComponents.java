package atmMVC.controller;

import atmMVC.model.Account;
import atmMVC.model.CreditCardAccount;
import atmMVC.model.Customer;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

/**
 * Common components of Customer in the UI.
 */
public class CustomerCommonComponents extends CommonComponents {
    protected Customer customer;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param customer Customer
     */
    public void setCustomer(Customer customer) { this.customer = customer;}

    protected boolean canTransferOutMoney(Account account, TextField amountField) {
        String errorMessage = "";
        if(account instanceof CreditCardAccount) {
            errorMessage += "Cannot transfer out money from Credit Card Account!\n";
        }

        if(!account.isAbleToTakeOutMoney(new BigDecimal(amountField.getText()))) {
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
