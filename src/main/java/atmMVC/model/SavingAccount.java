package atmMVC.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * A savings account.
 */
public class SavingAccount extends AssetAccount {
    static double monthlyInterestRate = 0.001;

    /**
     * Constructs a new savings account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public SavingAccount(int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(customerNumber, amount, createdOn);
        this.accountType = "SavingAccount";
    }

    /**
     * Constructs a new debt account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public SavingAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(accountNumber, customerNumber, amount, createdOn);
        this.accountType = "SavingAccount";
    }

    /**
     * Checks whether this account is able to take out a certain amount of money
     * @param amount the amount of money that is being checked
     */
    public boolean isAbleToTakeOutMoney(BigDecimal amount) {
        if(this.getBalance().compareTo(amount) < 0) {
            System.out.println("This SavingAccount does not have enough money.");
            return false;
        }
        return true;
    }
    protected void addInterest() {
        BigDecimal currentBalance = this.getBalance();
        BigDecimal interestRate = BigDecimal.valueOf(monthlyInterestRate);
        BigDecimal interest = currentBalance.multiply(interestRate);
        this.setBalance(currentBalance.add(interest).setScale(2, RoundingMode.HALF_UP));
    }
}
