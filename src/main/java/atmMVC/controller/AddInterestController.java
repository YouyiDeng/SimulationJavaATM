package atmMVC.controller;

import atmMVC.controller.BankCommonComponents;
import atmMVC.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A controller that handles adding interest to Savings Accounts within the UI.
 */
public class AddInterestController extends BankCommonComponents {
    @FXML
    private Label messageLabel;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public AddInterestController() {

    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleAddInterest() {
        String message = messageLabel.getText() + "\n";

        LocalDate firstDayOfThisMonth = LocalDate.of(
                LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        Bank bank = new Bank(firstDayOfThisMonth);
        mainApp.setBank(bank);

        message += "After adding interest, SavingAccounts total is: " + savingAccountTotal().toString();
        messageLabel.setText(message);
    }

    /**
     * Called when the user clicks close.
     */
    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    public void setInitialMessageLabel() {
        messageLabel.setText("SavingAccounts total is: " + savingAccountTotal().toString());
    }
    private BigDecimal savingAccountTotal() {
        BigDecimal total = BigDecimal.valueOf(0);
        ArrayList<SavingAccount> savingAccounts = this.mainApp.getBank().getSavingAccounts();
        for (SavingAccount savingAccount: savingAccounts)
            total = total.add(savingAccount.getBalance());

        return total;
    }
}
