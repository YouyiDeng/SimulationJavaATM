package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A chequing account.
 */

public class ChequingAccount extends AssetAccount {
    private boolean primary;
    private BigDecimal overDraftLimit = BigDecimal.valueOf(100.00);

    /**
     * Constructs a new chequing account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     * @param isPrimary the status of the account (default vs non-default)
     */
    public ChequingAccount(int customerNumber, BigDecimal amount, LocalDateTime createdOn, boolean isPrimary) {
        super(customerNumber, amount, createdOn);
        this.accountType = "ChequingAccount";
        this.primary = isPrimary;
    }

    /**
     * Constructs a new debt account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     * @param isPrimary the status of the account (default vs non-default)
     */
    public ChequingAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn,
                           boolean isPrimary) {
        super(accountNumber, customerNumber, amount, createdOn);
        this.accountType = "ChequingAccount";
        this.primary = isPrimary;
    }

    /**
     * Checks whether the current chequing account is the primary account
     *
     * @return true if the current account is the primary account
     */
    public boolean getPrimary() {return this.primary;}
    void setPrimary(boolean isPrimary) {this.primary = isPrimary;}

    /**
     * Checks whether this account is able to take out an amount of money
     * @param amount the amount of money that is being checked
     */
    public boolean isAbleToTakeOutMoney(BigDecimal amount) {
        if(this.getBalance().compareTo(BigDecimal.valueOf(0.0)) < 0) {
            System.out.println("This ChequingAccount has negative balance.");
            return false;
        }
        if((this.getBalance().add(this.overDraftLimit)).compareTo(amount) < 0) {
            System.out.println("This ChequingAccount will exceed over draft limit.");
            return false;
        }
        return true;
    }
}
