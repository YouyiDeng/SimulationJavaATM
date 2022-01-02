package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.*;

/**
 * A transaction to be displayed using JavaFX.
 */
public class  TransactionForFX {
    private final IntegerProperty trxID;
    private final IntegerProperty customerNumber;
    private final IntegerProperty accountNumber;
    private final ObjectProperty<BigDecimal> trxAmount;
    private final StringProperty trxType;
    private final ObjectProperty<LocalDateTime> trxDateTime;
    private final IntegerProperty secondNumber;
    private final BooleanProperty undoable;
    private final BooleanProperty isReversingEntry;


    /**
     * Constructs a new transaction using listenable fields to be used for display in TableView
     *
     * @param trxID the account number of the account that the transaction is occurring from
     * @param customerNumber the customer number of the customer that made the transaction
     * @param accountNumber the account number of the account that the transaction is occurring from
     * @param trxAmount the amount of the transaction
     * @param trxType the type of the transaction
     * @param trxDateTime the time the transaction is made
     * @param secondNumber represents either an account number/customer number/payee number/0 depending on the type of transaction
     * @param undoable whether the transaction is undoable or not
     * @param isReversingEntry whether the transaction has been reversed or not
     */
    public TransactionForFX(int trxID, int customerNumber, int accountNumber, BigDecimal trxAmount, String trxType,
                            LocalDateTime trxDateTime, int secondNumber, boolean undoable, boolean isReversingEntry) {
        this.trxID = new SimpleIntegerProperty(trxID);
        this.customerNumber = new SimpleIntegerProperty(customerNumber);
        this.accountNumber = new SimpleIntegerProperty(accountNumber);
        this.trxAmount = new SimpleObjectProperty<BigDecimal>(trxAmount);
        this.trxType = new SimpleStringProperty(trxType);
        this.trxDateTime = new SimpleObjectProperty<LocalDateTime>(trxDateTime);
        this.secondNumber = new SimpleIntegerProperty(secondNumber);
        this.undoable = new SimpleBooleanProperty(undoable);
        this.isReversingEntry = new SimpleBooleanProperty(isReversingEntry);
    }

    public int getTrxID() {
        return trxID.get();
    }
    public int getCustomerNumber() {
        return customerNumber.get();
    }
    public int getAccountNumber() {
        return accountNumber.get();
    }

    public BigDecimal getTrxAmount() {
        return trxAmount.get();
    }
    public LocalDateTime getTrxDateTime() {
        return trxDateTime.get();
    }

    public StringProperty trxTypeProperty() {
        return trxType;
    }

    public int getSecondNumber() {
        return secondNumber.get();
    }

    public BooleanProperty undoableProperty() {
        return undoable;
    }

    public BooleanProperty isReversingEntryProperty() {
        return isReversingEntry;
    }
}
