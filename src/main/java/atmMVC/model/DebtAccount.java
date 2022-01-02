package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A Debt Account.
 */
public abstract class DebtAccount extends Account {
    protected BigDecimal maximumDebt;

    /**
     * Constructs a new debt account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public DebtAccount(int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(customerNumber, amount, createdOn);
    }

    /**
     * Constructs a new debt account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public DebtAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(accountNumber, customerNumber, amount, createdOn);
    }

    /**
     * Adds a certain amount to this account
     *
     * @param amount the amount that is being added
     */
    protected void addAmount(BigDecimal amount) {
        this.setBalance(this.getBalance().subtract(amount));
    }

    /**
     * Subtracts a certain amount from this account.
     *
     * @param amount the amount that is being subtracted
     */
    protected void subtractAmount(BigDecimal amount) {
        this.setBalance(this.getBalance().add(amount));
    }

    /**
     * Checks whether this account is able to take out a certain amount of money
     * @param amount the amount of money that is being checked
     */
    public boolean isAbleToTakeOutMoney(BigDecimal amount) {
        BigDecimal newBalance = this.getBalance().add(amount);
        if(newBalance.compareTo(this.maximumDebt) > 0) {
            System.out.println("You have exceeded the maximum amount of debt for this account!");
            return false;
        }
        return true;
    }
    public BigDecimal getMaximumDebt() {return maximumDebt;}
    public void setMaximumDebt(BigDecimal maximumDebt) {
        this.maximumDebt = maximumDebt;
    }
}
