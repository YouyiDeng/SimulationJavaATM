package atmMVC.controller;

import atmMVC.controller.BankCommonComponents;
import atmMVC.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A controller that handles processing a new account request in the UI.
 */
public class ProcessNewAccountRequestController extends BankCommonComponents {
    @FXML
    private TableView<NewAccountRequestForFX> accountRequestTable;
    @FXML
    private TableColumn<NewAccountRequestForFX, Integer> customerNumberColumn;
    @FXML
    private TableColumn<NewAccountRequestForFX, String> accountTypeColumn;
    @FXML
    private TableColumn<NewAccountRequestForFX, LocalDateTime> requestDateTimeColumn;


    private ObservableList<NewAccountRequestForFX> accountRequests = FXCollections.observableArrayList();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ProcessNewAccountRequestController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the table with the columns.
        customerNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCustomerNumber()).asObject());
        accountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());

        requestDateTimeColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getRequestDateTime()));
    }

    public void setAccountRequestsTable() {
        accountRequests = ((BankManager)this.bankPerson).getNewAccountRequestsFX();
        accountRequestTable.setItems(accountRequests);
    }

    /**
     * Called when the user clicks approve.
     */
    @FXML
    private void handleApprove() {
        if(isInputValid()) {
            int selectedIndex = accountRequestTable.getSelectionModel().getSelectedIndex();
            NewAccountRequestForFX accountRequestForFX = accountRequestTable.getSelectionModel().getSelectedItem();
            boolean result = ((BankManager)this.bankPerson).createAccount(accountRequestForFX);
            String message;
            if(result) {
                accountRequestTable.getItems().remove(selectedIndex);
                accountRequestTable.refresh();
                // update account request file
                ObservableList<NewAccountRequestForFX> leftOverRequests = accountRequestTable.getItems();
                ((BankManager)this.bankPerson).updateAccountRequestFile(leftOverRequests);
                // Show the confirm message.
                message = accountRequestForFX.accountTypeProperty().getValueSafe() +
                        " has been created for customer " + accountRequestForFX.getCustomerNumber();
                showAlert("ATM MVC", "Successful",
                        message, Alert.AlertType.INFORMATION);
            }
            else {
                message = "New account cannot be created for customer " + accountRequestForFX.getCustomerNumber();
                showAlert("ATM MVC", "Failed",
                        message, Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Called when the user clicks decline.
     */
    @FXML
    private void handleDecline() {
        if(isInputValid()) {
            int selectedIndex = accountRequestTable.getSelectionModel().getSelectedIndex();
            accountRequestTable.getItems().remove(selectedIndex);
            accountRequestTable.refresh();
            // update account request file
            ObservableList<NewAccountRequestForFX> leftOverRequests = accountRequestTable.getItems();
            ((BankManager)this.bankPerson).updateAccountRequestFile(leftOverRequests);
            String message = "Account request has been declined";
            showAlert("ATM MVC", "Declined",
                    message, Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    /**
     * Validates the user select an entry
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if(accountRequestTable.getSelectionModel().getSelectedIndex() < 0 ) {
            errorMessage += "Please select a request in the table.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "Please correct error",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }
}
