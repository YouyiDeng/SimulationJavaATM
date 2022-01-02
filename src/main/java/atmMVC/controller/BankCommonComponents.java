package atmMVC.controller;

import atmMVC.MainApp;
import atmMVC.model.ATM;
import atmMVC.model.BankPerson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller that handles common functionality of the Bank in the UI.
 */
public class BankCommonComponents extends CommonComponents {
    protected BankPerson bankPerson;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param bankPerson BankPerson
     */
    protected void setBankPerson(BankPerson bankPerson) { this.bankPerson = bankPerson;}

    private String atmMoney() {
        String atmMoney = "ATM has total of $" + ATM.getATMMoney() + "CAD, and $" + ATM.getATMMoneyUSD() + "USD" + "\n" +
                "$5   Bills (CAD, USD): " + ATM.getBillOf5() + ", " + ATM.getBillOf5USD() + "\n" +
                "$10 Bills (CAD, USD): " + ATM.getBillOf10() + ", " + ATM.getBillOf10USD() + "\n" +
                "$20 Bills (CAD, USD): " + ATM.getBillOf20() + ", " + ATM.getBillOf20USD() + "\n" +
                "$50 Bills (CAD, USD): " + ATM.getBillOf50() + ", " + ATM.getBillOf50USD() + "\n";
        return atmMoney;
    }
    /**
     * Shows the add money view
     */
    protected void showAddMoneyDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AtmMoney.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Money To ATM");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            AtmMoneyController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the join account view
     */
    protected void showMakeJointAccountDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/JoinAccount.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Make a Joint Account");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            JoinAccountController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setAccountTable();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the user clicks View ATM Balance.
     */
    @FXML
    protected void handleViewATMBalance() {
        // Show the message.
        showAlert("ATM MVC", "Money Inside ATM", this.atmMoney(), Alert.AlertType.INFORMATION);
    }

    /**
     * Called when the user clicks Add Money To ATM.
     */
    @FXML
    protected void handleAddMoneyToATM() {
        showAddMoneyDialog();
    }

    /**
     * Called when the user clicks Process Deposit File
     */
    @FXML
    protected void handleProcessDepositFile() {
        String message = this.bankPerson.processDepositFile();
        // Show the message.
        showAlert("ATM MVC", "Process Deposit File", message, Alert.AlertType.INFORMATION);

    }
    /**
     * Called when the user clicks make a joint account
     */
    @FXML
    protected void handleMakeJointAccount() {
        showMakeJointAccountDialog();
    }
    /**
     * Called when the user clicks My Accounts Overview
     */
    @FXML
    protected void handleMyAccountsOverview() {
        mainApp.showAccountsOverviewDialog(this.bankPerson.getMySelfAsCustomer());
    }
    @FXML
    protected void handleLookupCustomers() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LookupCustomers.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Lookup Customers");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            LookupCustomersController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setComboBox();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
