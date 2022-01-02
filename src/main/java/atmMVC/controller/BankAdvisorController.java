package atmMVC.controller;

import atmMVC.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller that handles Bank Advisor functionality in the UI.
 */
public class BankAdvisorController extends BankCommonComponents {

    /**
     * Called when the user clicks view my appointment
     */
    @FXML
    private void handleViewMyAppointment() {
        showAppointmentDialog();
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
     * Shows my Appointment view
     */
    private void showAppointmentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MyAppointments.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("My Appointments");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the controller.
            MyAppointmentsController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setBankPerson(this.bankPerson);
            controller.setAppointmentTable();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
