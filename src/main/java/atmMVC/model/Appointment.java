package atmMVC.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * An appointment with a bank adviser
 */
public class Appointment {
    private final IntegerProperty bankAdvisorNumber;
    private final IntegerProperty customerNumber;
    private final ObjectProperty<LocalDateTime> requestedOn;
    private final StringProperty subject;

    /**
     * Constructs a new appointment with a bank adviser.
     *
     * @param bankAdvisorNumber this bank adviser number
     * @param customerNumber requested for this customer number
     * @param requestedOn date time requested
     * @param subject subject of appointment
     */
    public Appointment(int bankAdvisorNumber, int customerNumber, LocalDateTime requestedOn, String subject) {
        this.bankAdvisorNumber = new SimpleIntegerProperty(bankAdvisorNumber);
        this.customerNumber = new SimpleIntegerProperty(customerNumber);
        this.requestedOn = new SimpleObjectProperty<LocalDateTime>(requestedOn);
        this.subject = new SimpleStringProperty(subject);
    }

    public int getBankAdvisorNumber() {
        return bankAdvisorNumber.get();
    }
    public int getCustomerNumber() { return customerNumber.get();}
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber.set(customerNumber);
    }
    public IntegerProperty customerNumberProperty() {
        return customerNumber;
    }


    public LocalDateTime getRequestedOn() {
        return requestedOn.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }
}
