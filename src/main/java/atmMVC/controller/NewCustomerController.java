package atmMVC.controller;

import atmMVC.controller.CommonComponents;
import atmMVC.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * A controller that handles making a new customer in the UI.
 */
public class NewCustomerController extends CommonComponents {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField sinNumberdField;

    /**
     * Called when the user clicks Create.
     */
    @FXML
    private void handleCreate() {
        if (isInputValid()) {
            if(mainApp.getBank().userNameTaken(userNameField.getText(), true)) {
                // Username is already taken
                showAlert("ATM MVC", "Please create new customer username",
                        "Username " + userNameField.getText() + " is already taken",
                        Alert.AlertType.ERROR);
            }
            else if(mainApp.getBank().sinNumberTaken(
                    Integer.parseInt(sinNumberdField.getText().trim()), true)) {
                // SIN number is already taken
                showAlert("ATM MVC", "Failed",
                        "SIN number is already used", Alert.AlertType.ERROR);
            }
            else {
                Customer newCustomer = new Customer(userNameField.getText().trim(),
                        passwordField.getText().trim(), Integer.parseInt(sinNumberdField.getText().trim()));
                this.mainApp.getBank().addNewCustomer(newCustomer);
                showAlert("ATM MVC", "New customer is created", "Successful!",
                        Alert.AlertType.INFORMATION);
                // show some info successful
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
