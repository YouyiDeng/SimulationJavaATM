package atmMVC.controller;

import atmMVC.*;
import atmMVC.model.BankManager;
import atmMVC.model.BankPerson;
import atmMVC.model.BankAdvisor;
import atmMVC.model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * A controller that handles logging into the UI.
 */
public class LoginController extends CommonComponents {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;

    /**
     * Called when the user clicks Customer Login.
     */
    @FXML
    private void handleCustomerLogin() {
        if (isInputValid()) {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            Customer c = mainApp.getBank().findCustomer(userName, password);
            if (c != null) {
                showCustomerView(c);
            }
            else {   // Cannot find, username and/or password wrong
                showAlert("ATM MVC", "Login Failed",
                        "Sorry, username and/or password not correct.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Called when the user clicks Bank Login.
     */
    @FXML
    private void handleBankLogin() {
        if (isInputValid()) {
            String userName = userNameField.getText();
            String password = passwordField.getText();

            BankPerson w = mainApp.getBank().findWorker(userName, password);
            if (w instanceof BankManager) {
                showBankManagerView((BankManager)w);
            }
            else if (w instanceof BankAdvisor) {  // Bank Staff
                showBankAdvisorView((BankAdvisor)w);
            } else {   // Cannot find, username and/or password wrong
                showAlert("ATM MVC", "Login Failed",
                        "Sorry, username and/or password not correct.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        userNameField.setText("");
        passwordField.setText("");
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

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("ATM MVC", "Please correct invalid fields",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }

    private void showBankManagerView(BankManager bankManager) {
        try {
            // Load bank manager view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BankManager.fxml"));
            AnchorPane loginView = loader.load();

            this.mainApp.getPrimaryStage().setTitle("ATM MVC - Bank Manager");

            // Set view into the center of root layout.
            mainApp.getRootLayout().setCenter(loginView);
            // Give the controller access to the main app.
            BankManagerController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(bankManager);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBankAdvisorView(BankAdvisor bankAdvisor) {
        try {
            // Load bank manager view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BankAdvisor.fxml"));
            AnchorPane loginView = loader.load();

            this.mainApp.getPrimaryStage().setTitle("ATM MVC - Bank Advisor");

            // Set view into the center of root layout.
            mainApp.getRootLayout().setCenter(loginView);
            // Give the controller access to the main app.
            BankAdvisorController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(bankAdvisor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCustomerView(Customer customer) {
        try {
            // Load customer view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Customer.fxml"));
            AnchorPane loginView = loader.load();

            this.mainApp.getPrimaryStage().setTitle("ATM MVC - Customer");

            // Set view into the center of root layout.
            mainApp.getRootLayout().setCenter(loginView);
            // Give the controller access to the main app.
            CustomerController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setCustomer(customer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
