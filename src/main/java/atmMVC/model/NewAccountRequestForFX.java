package atmMVC.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * A new request for a bank account to be displayed using JavaFX.
 */
public class NewAccountRequestForFX {
    private final IntegerProperty customerNumber;
    private final StringProperty accountType;
    private final ObjectProperty<LocalDateTime> requestDateTime;

    /**
     * Constructs a new account request using listenable fields to be used for display in TableView.
     *
     * @param customerNumber the customer number of the customer who requested this account
     * @param accountType the type of account that is requested
     * @param requestDateTime  the time that the request occurred
     */
    public NewAccountRequestForFX(int customerNumber, String accountType, LocalDateTime requestDateTime) {
        this.customerNumber = new SimpleIntegerProperty(customerNumber);
        this.accountType = new SimpleStringProperty(accountType);
        this.requestDateTime = new SimpleObjectProperty<LocalDateTime>(requestDateTime);
    }

    public int getCustomerNumber() {
        return customerNumber.get();
    }
    public StringProperty accountTypeProperty() {
        return accountType;
    }
    public LocalDateTime getRequestDateTime() {
        return requestDateTime.get();
    }
}
