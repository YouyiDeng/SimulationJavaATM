package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A bank account.
 */
public abstract class Account {
    static int nextAccountNumber = 1;
    protected String accountType;
    protected int accountNumber;
    protected int customerNumber;
    protected BigDecimal balance;
    protected LocalDateTime createdOn;
    protected int recentTrxID;
    protected boolean joint;
    protected Customer secondCustomer;

    /**
     * Constructs a new account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public Account(int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        this.customerNumber = customerNumber;
        this.balance = amount;
        this.createdOn = createdOn;
        this.accountNumber = nextAccountNumber;
        nextAccountNumber ++;
        joint = false;
        secondCustomer = null;
    }

    /**
     * Constructs a new account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public Account(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        this.accountNumber = accountNumber;
        this.customerNumber = customerNumber;
        this.balance = amount;
        this.createdOn = createdOn;
        joint = false;
        secondCustomer = null;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    void setBalance(BigDecimal amount) {balance = amount;}
    /**
     * Checks whether this account is able to take out a certain amount of money
     * @param amount the amount of money that is being checked
     */
    public abstract boolean isAbleToTakeOutMoney(BigDecimal amount);

    /**
     * Adds a certain amount to this account
     *
     * @param amount the amount that is being added
     */
    abstract void addAmount(BigDecimal amount);

    /**
     * Subtracts a certain amount from this account.
     *
     * @param amount the amount that is being subtracted
     */
    abstract void subtractAmount(BigDecimal amount);

    public int getAccountNumber() {return accountNumber;}
    public int getCustomerNumber() {
        return customerNumber;
    }
    public int getRecentTrxID() {
        return recentTrxID;
    }

    public boolean getJoint() {
        return this.joint;
    }
    public void setJoint(boolean value) {
        this.joint = value;
    }
    public Customer getSecondCustomer() {
        return this.secondCustomer;
    }
    public void setSecondCustomer(Customer customer) {
        this.secondCustomer = customer;
    }
}
