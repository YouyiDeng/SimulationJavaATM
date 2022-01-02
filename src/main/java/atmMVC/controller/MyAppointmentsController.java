package atmMVC.controller;

import atmMVC.controller.BankCommonComponents;
import atmMVC.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A controller that handles appointments in the UI.
 */
public class MyAppointmentsController extends BankCommonComponents {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> customerNumberColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> requestedOnColumn;
    @FXML
    private TableColumn<Appointment, String> subjectColumn;


    @FXML
    private TextField customerNumberField;
    @FXML
    private TextField subjectField;


    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MyAppointmentsController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the able with the columns.
        customerNumberColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCustomerNumber()).asObject());

        requestedOnColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<LocalDateTime>(cellData.getValue().getRequestedOn()));

        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
    }

    public void setAppointmentTable() {
        appointments = ((BankAdvisor)this.bankPerson).getAppointments();
        appointmentTable.setItems(appointments);
    }

    /**
     * Called when the user clicks make a join.
     */
    @FXML
    private void handleMakeAppointment() {
        if(isInputValid())
            makeNewAppointment();
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
     * Validates the user select an account
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";


        if(!isNumeric(customerNumberField.getText())) {
            errorMessage += "Customer number has to be a number.\n";
        }
        else {
            Customer customer = Bank.findCustomer(Integer.parseInt(customerNumberField.getText()));
            if(customer == null)
                errorMessage += "The customer " + customerNumberField.getText() + " doesn't exist.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("ATM MVC", "Please correct customer field",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }

    private void makeNewAppointment() {
        Appointment newAppointment = ((BankAdvisor)this.bankPerson).createAppointment(
                Integer.parseInt(customerNumberField.getText()), subjectField.getText());
        appointmentTable.refresh();

        // reset fields
        customerNumberField.setText("");
        subjectField.setText("");
    }
}
