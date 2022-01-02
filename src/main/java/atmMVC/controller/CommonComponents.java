package atmMVC.controller;

import atmMVC.MainApp;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Common components of the UI.
 */
public class CommonComponents {
    protected MainApp mainApp;
    protected  Stage dialogStage;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp MainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage Stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Show alert dialogue
     *
     * @param title      String
     * @param headerText String
     * @param message    String
     * @param alertType  Alert.AlertType
     */
    protected void showAlert(String title, String headerText, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Check is str is number
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check is str is number
     *
     * @param str String
     * @return boolean
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
