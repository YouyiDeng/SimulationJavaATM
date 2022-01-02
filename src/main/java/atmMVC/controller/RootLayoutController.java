package atmMVC.controller;

import atmMVC.controller.CommonComponents;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * A controller that handles the root layout of the UI.
 */
public class RootLayoutController extends CommonComponents {
    @FXML
    private void handleHelpContactUs() {
        // Show the error message.
        showAlert("ATM MVC", "Contact Us",
                "Please call 416-834-6277", Alert.AlertType.INFORMATION);
    }
}
