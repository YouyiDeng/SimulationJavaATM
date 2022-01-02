package atmMVC.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

/**
 * A controller that handles new account requests in the UI.
 */
public class NewAccountRequestController extends CustomerCommonComponents {
    @FXML
    private CheckBox chequingBox;
    @FXML
    private CheckBox savingBox;
    @FXML
    private CheckBox powerSavingBox;
    @FXML
    private CheckBox creditCardBox;
    @FXML
    private CheckBox lineOfCreditBox;

    /**
     * Called when the user clicks Create.
     */
    @FXML
    private void handleSendRequest() {
        String message = "";
        if (isInputValid()) {
            if(chequingBox.isSelected()) {
                message += this.customer.requestNewAccount("ChequingAccount") + "\n";
            }
            if(savingBox.isSelected()) {
                message += this.customer.requestNewAccount("SavingAccount") + "\n";
            }
            if(powerSavingBox.isSelected()) {
                message += this.customer.requestNewAccount("PowerSavingAccount") + "\n";
            }
            if(creditCardBox.isSelected()) {
                message += this.customer.requestNewAccount("CreditCardAccount") + "\n";
            }
            if(lineOfCreditBox.isSelected()) {
                message += this.customer.requestNewAccount("LineOfCreditAccount") + "\n";
            }

            showAlert("ATM MVC", "New Account Request", message,
                        Alert.AlertType.INFORMATION);
            // show some info successful
            dialogStage.close();
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
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (!chequingBox.isSelected() && !savingBox.isSelected() && !powerSavingBox.isSelected() &&
                !creditCardBox.isSelected() && !lineOfCreditBox.isSelected()) {
            errorMessage += "None of checkbox is selected!\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "Please choose one or more accounts",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }
}
