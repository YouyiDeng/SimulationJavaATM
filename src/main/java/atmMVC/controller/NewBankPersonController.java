package atmMVC.controller;

import atmMVC.controller.BankCommonComponents;
import atmMVC.model.BankAdvisor;
import atmMVC.model.BankManager;
import atmMVC.model.BankPerson;
import atmMVC.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * A controller that handles making a new bank person in the UI.
 */
public class NewBankPersonController extends BankCommonComponents {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField sinNumberdField;
    @FXML
    private ChoiceBox<String> roleField;

    /**
     * Called when the user clicks Create.
     */
    @FXML
    private void handleCreate() {
        if (isInputValid()) {
            if (mainApp.getBank().userNameTaken(userNameField.getText(), false)) {
                // Username is already taken
                showAlert("ATM MVC", "Please create new bank person username",
                        "Username " + userNameField.getText() + " is already taken",
                        Alert.AlertType.ERROR);
            }
            else if(mainApp.getBank().sinNumberTaken(
                    Integer.parseInt(sinNumberdField.getText().trim()), false)) {
                // SIN number is already taken
                showAlert("ATM MVC", "Failed",
                        "SIN number is already used", Alert.AlertType.ERROR);
            }
            else {
                BankPerson p = null;
                if(roleField.getValue().equalsIgnoreCase("Advisor")) {
                    p = new BankAdvisor(userNameField.getText().trim(),
                        passwordField.getText().trim(), Integer.parseInt(sinNumberdField.getText().trim()));
                }
                else if (roleField.getValue().equalsIgnoreCase("Manager")) {
                    p = new BankManager(userNameField.getText().trim(),
                            passwordField.getText().trim(), Integer.parseInt(sinNumberdField.getText().trim()));
                }

                if (p !=null) {
                    this.mainApp.getBank().addNewWorker(p);
                    // show some info successful
                    showAlert("ATM MVC", "Successful", "New bank person is created!",
                            Alert.AlertType.INFORMATION);

                }
                else {
                    showAlert("ATM MVC", "Failed", "Cannot create New bank person!",
                            Alert.AlertType.ERROR);
                }
                dialogStage.close();

            }
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

        if (userNameField.getText() == null || userNameField.getText().length() == 0) {
            errorMessage += "Username is blank!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Password is blank!\n";
        }
        if (!isNumeric(sinNumberdField.getText())) {
            errorMessage += "SIN number is blank or not a number!\n";
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
}
