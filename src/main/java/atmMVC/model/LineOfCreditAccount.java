package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A line of credit account.
 */
public class LineOfCreditAccount extends DebtAccount {

    /**
     * Constructs a new line of credit account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public LineOfCreditAccount(int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(customerNumber, amount, createdOn);
        this.accountType = "LineofCreditAccount";
        this.setMaximumDebt(BigDecimal.valueOf(3000));
    }
    /**
     * Constructs a new line of credit account. This constructor is used specifically
     * when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public LineOfCreditAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(accountNumber, customerNumber, amount, createdOn);
        this.accountType = "LineOfCreditAccount";
        this.setMaximumDebt(BigDecimal.valueOf(3000));
    }
}
