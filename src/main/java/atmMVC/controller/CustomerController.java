package atmMVC.controller;

import atmMVC.MainApp;
import atmMVC.model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller that handles Customer functionality in the UI.
 */
public class CustomerController extends CustomerCommonComponents {
    /**
     * Called when the user clicks request new account
     */
    @FXML
    private void handleRequestNewAccount() {
        showNewAccountRequestDialog();
    }

    /**
     * Called when the user clicks deposit
     */
    @FXML
    private void handleDeposit() {
        showDepositWithdrawalDialog(true);
    }

    /**
     * Called when the user clicks withdraw
     */
    @FXML
    private void handleWithdraw() {
        showDepositWithdrawalDialog(false);
    }

    /**
     * Called when the user clicks transfer in.
     */
    @FXML
    private void handleTransferIn() {
        showTransferDialog(true);
    }

    /**
     * Called when the user clicks transfer out.
     */
    @FXML
    private void handleTransferOut() {
        showTransferDialog(false);
    }

    /**
     * Called when the user clicks transfer out to other.
     */
    @FXML
    private void handleTransferToOther() {
        showTransferToOtherPayBillDialog(true);
    }

    /**
     * Called when the user clicks pay bill.
     */
    @FXML
    private void handlePayBill() {
        showTransferToOtherPayBillDialog(false);
    }

    /**
     * Called when the user clicks accounts overview.
     */
    @FXML
    private void handleAccountsOverview() {
        mainApp.showAccountsOverviewDialog(this.customer);
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
     * Shows the account request view
     */
    private void showNewAccountRequestDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/NewAccountRequest.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Request New Account");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            NewAccountRequestController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setCustomer(this.customer);
            controller.setDialogStage(dialogStage);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the deposit ow withdrawal view
     */
    private void showDepositWithdrawalDialog(boolean isDeposit) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/DepositWithdrawal.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            if(isDeposit)
                dialogStage.setTitle("Deposit");
            else
                dialogStage.setTitle("Withdrawal");

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            DepositWithdrawalController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setCustomer(this.customer);
            controller.setAccountTable();
            controller.setControlNames(isDeposit);
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the transfer view
     */
    private void showTransferDialog(boolean isTransferIn) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Transfer.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Transfer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            TransferController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setCustomer(this.customer);
            controller.setAccountTables();
            controller.setControlNames(isTransferIn);
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the transfer out to other or pay bill view
     */
    private void showTransferToOtherPayBillDialog(boolean isTransferToOther) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TransferToOtherPayBill.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            if(isTransferToOther)
                dialogStage.setTitle("Transfer Out To Other");
            else
                dialogStage.setTitle("Pay Bill");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            TransferToOtherPayBillController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setCustomer(this.customer);
            controller.setAccountTable();
            controller.setControlNames(isTransferToOther);
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
