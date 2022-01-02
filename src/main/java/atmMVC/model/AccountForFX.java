package atmMVC.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A bank account to be displayed using JavaFX.
 */
public class AccountForFX {
    private final IntegerProperty accountNumber;
    private final StringProperty accountType;
    private final ObjectProperty<BigDecimal> balance;
    private final ObjectProperty<LocalDateTime> accountOpenDate;
    private final BooleanProperty primary;
    private final BooleanProperty joint;

    /**
     * Constructs a new account using listenable fields to be used for display in TableView.
     *
     * @param accountNumber the customer number of the customer who owns this account
     * @param accountType the type of account
     * @param balance  the display balance of this account
     * @param accountOpenDate  the date and time this account was created on
     * @param primary  the status of the account (primary vs. non-primary)
     * @param joint whether the account is joint or personal
     */
    public AccountForFX(int accountNumber, String accountType, BigDecimal balance,
                        LocalDateTime accountOpenDate, boolean primary, boolean joint) {
        this.accountNumber = new SimpleIntegerProperty(accountNumber);
        this.accountType = new SimpleStringProperty(accountType);
        this.balance = new SimpleObjectProperty<BigDecimal>(balance);
        this.accountOpenDate = new SimpleObjectProperty<LocalDateTime>(accountOpenDate);
        this.primary = new SimpleBooleanProperty(primary);
        this.joint = new SimpleBooleanProperty(joint);
    }
    public int getAccountNumber() {
        return accountNumber.get();
    }
    public StringProperty accountTypeProperty() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance.get();
    }
    public void setBalance(BigDecimal value) {this.balance.set(value);}

    public LocalDateTime getAccountOpenDate() {
        return accountOpenDate.get();
    }
    public BooleanProperty primaryProperty() {
        return primary;
    }
    public BooleanProperty jointProperty() {
        return joint;
    }
    public void setJoint(Boolean joint) { this.jointProperty().setValue(joint);}
}
