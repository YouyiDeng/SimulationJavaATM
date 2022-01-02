package atmMVC.controller;

import atmMVC.model.ATM;
import atmMVC.model.BankManager;
import javafx.fxml.FXML;

import atmMVC.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller that handles functionality of the Bank Manager in the UI.
 */
public class BankManagerController extends BankCommonComponents {
    /**
     * Called when the user clicks Create A New Customer
     */
    @FXML
    private void handleCreateNewCustomer() {
        mainApp.showNewCustomerDialog();
    }

    /**
     * Called when the user clicks Create A New BankPerson
     */
    @FXML
    private void handleCreateNewBankPerson() {
        mainApp.showNewBankPersonDialog();
    }

    /**
     * Called when the user clicks Process New Account Request
     */
    @FXML
    private void handleProcessNewAccountRequest() {
        showProcessNewAccountRequestDialog();
    }

    /**
     * Called when the user clicks Add Interest To Savings.
     */
    @FXML
    private void handleAddInterestToSavings() {
        showAddInterestDialog();
    }

    /**
     * Called when the user clicks Undo Most Recent Transaction.
     */
    @FXML
    private void handleUndoMostRecentTransaction() {
        boolean result = ((BankManager)this.bankPerson).undoMostRecentTransaction();
        String message;
        if(result)
            message = "Transaction has been reversed.";
        else
            message = "There is no recent transaction today.";
        // Show the message.
        showAlert("ATM MVC", "Undo Most Recent Transaction", message, Alert.AlertType.INFORMATION);
    }

    /**
     * Called when the user clicks Undo Particular Transaction.
     */
    @FXML
    private void handleUndoParticularTransaction() {
        showUndoTransactionDialog();
    }

    /**
     * Called when the user clicks Exit.
     */
    @FXML
    private void handleExit() {
        this.mainApp.getPrimaryStage().setTitle("ATM MVC - Login");
        mainApp.showLoginView();
    }

    /**
     * Shows the process new account request view
     */
    private void showProcessNewAccountRequestDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ProcessNewAccountRequest.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Process New Account Request");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            ProcessNewAccountRequestController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setAccountRequestsTable();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the undo transaction view
     */
    private void showUndoTransactionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/UndoTransaction.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Undo Transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            UndoTransactionController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setTrxTable();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the add add interest view
     */
    private void showAddInterestDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddInterest.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Interest");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            AddInterestController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setInitialMessageLabel();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
