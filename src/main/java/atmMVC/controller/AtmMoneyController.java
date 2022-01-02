package atmMVC.controller;

import atmMVC.model.ATM;
import atmMVC.model.BankManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * A controller handles money in the ATM for the UI.
 */
public class AtmMoneyController extends BankCommonComponents {
    @FXML
    private TextField billOf5Field;
    @FXML
    private TextField billOf10Field;
    @FXML
    private TextField billOf20Field;
    @FXML
    private TextField billOf50Field;
    @FXML
    private TextField billOf5USDField;
    @FXML
    private TextField billOf10USDField;
    @FXML
    private TextField billOf20USDField;
    @FXML
    private TextField billOf50USDField;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        billOf5Field.setText("0");
        billOf10Field.setText("0");
        billOf20Field.setText("0");
        billOf50Field.setText("0");
        billOf5USDField.setText("0");
        billOf10USDField.setText("0");
        billOf20USDField.setText("0");
        billOf50USDField.setText("0");
    }

    /**
     * Called when the user clicks Create.
     */
    @FXML
    private void handleAddMoney() {
        if (isInputValid()) {
            int billOf5 = Integer.parseInt(billOf5Field.getText().trim());
            int billOf10 = Integer.parseInt(billOf10Field.getText().trim());
            int billOf20 = Integer.parseInt(billOf20Field.getText().trim());
            int billOf50 = Integer.parseInt(billOf50Field.getText().trim());
            int billOf5USD = Integer.parseInt(billOf5USDField.getText().trim());
            int billOf10USD = Integer.parseInt(billOf10USDField.getText().trim());
            int billOf20USD = Integer.parseInt(billOf20USDField.getText().trim());
            int billOf50USD = Integer.parseInt(billOf50USDField.getText().trim());

            int[] bills = {billOf5, billOf10, billOf20, billOf50, billOf5USD, billOf10USD, billOf20USD, billOf50USD};
            this.bankPerson.addMoneyToATM(bills);

            // Show total money in ATM
            String atmMoney = "ATM has total of $" + ATM.getATMMoney() + " CAD, and $" + ATM.getATMMoneyUSD() + " USD" + "\n" +
                    "$5   Bills (CAD, USD): " + ATM.getBillOf5() + ", " + ATM.getBillOf5USD() + "\n" +
                    "$10 Bills (CAD, USD): " + ATM.getBillOf10() + ", " + ATM.getBillOf10USD() + "\n" +
                    "$20 Bills (CAD, USD): " + ATM.getBillOf20() + ", " + ATM.getBillOf20USD() + "\n" +
                    "$50 Bills (CAD, USD): " + ATM.getBillOf50() + ", " + ATM.getBillOf50USD() + "\n";
            // Show the message.
            showAlert("ATM MVC", "Money has been added to ATM", atmMoney, Alert.AlertType.INFORMATION);
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

        if ( billOf5Field.getText() == null || !isNumeric(billOf5Field.getText())) {
            errorMessage += "$5 CAD Bill field is blank or not number!\n";
        }
        if ( billOf10Field.getText() == null || !isNumeric(billOf10Field.getText())) {
            errorMessage += "$10 CAD Bill field is blank or not number!\n";
        }
        if ( billOf20Field.getText() == null || !isNumeric(billOf20Field.getText())) {
            errorMessage += "$20 CAD Bill field is blank or not number!\n";
        }
        if ( billOf50Field.getText() == null || !isNumeric(billOf50Field.getText())) {
            errorMessage += "$50 CAD Bill field is blank or not number!\n";
        }
        if ( billOf5USDField.getText() == null || !isNumeric(billOf5USDField.getText())) {
            errorMessage += "$5 USD Bill field is blank or not number!\n";
        }
        if ( billOf10USDField.getText() == null || !isNumeric(billOf10USDField.getText())) {
            errorMessage += "$10 USD Bill field is blank or not number!\n";
        }
        if ( billOf20USDField.getText() == null || !isNumeric(billOf20USDField.getText())) {
            errorMessage += "$20 USD Bill field is blank or not number!\n";
        }
        if ( billOf50USDField.getText() == null || !isNumeric(billOf50USDField.getText())) {
            errorMessage += "$50 USD Bill field is blank or not number!\n";
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
