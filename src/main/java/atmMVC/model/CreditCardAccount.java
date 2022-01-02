package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A credit card account.
 */
public class CreditCardAccount extends DebtAccount {
    /**
     * Constructs a new credit card account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public CreditCardAccount(int customerNumber,  BigDecimal amount, LocalDateTime createdOn) {
        super(customerNumber, amount, createdOn);
        this.accountType = "CreditCardAccount";
        this.setMaximumDebt(BigDecimal.valueOf(1000));
    }

    /**
     * Constructs credit card account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public CreditCardAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(accountNumber, customerNumber, amount, createdOn);
        this.accountType = "CreditCardAccount";
        this.setMaximumDebt(BigDecimal.valueOf(1000));
    }
}
