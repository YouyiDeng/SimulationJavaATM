package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * An asset account.
 */
public abstract class AssetAccount extends Account {

    /**
     * Constructs a new asset account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public AssetAccount(int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(customerNumber, amount, createdOn);
    }

    /**
     * Constructs a new asset account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public AssetAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(accountNumber, customerNumber, amount, createdOn);
    }

    /**
     * Adds money to this Account.
     *
     * @param amount the amount that is being added
     */
    protected void addAmount(BigDecimal amount) {
        this.setBalance(this.getBalance().add(amount));
    }

    /**
     * Subtracts money from this Account.
     *
     * @param amount the amount that is being subtracted
     */
    protected void subtractAmount(BigDecimal amount) {
        this.setBalance(this.getBalance().subtract(amount));
    }
}
