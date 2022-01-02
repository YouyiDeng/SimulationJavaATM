package atmMVC.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * A power savings account, where users receive different interest rates depending on the amount saved in the account
 */
public class PowerSavingAccount extends SavingAccount{
    private double monthlyInterestRate;
    /**
     * Constructs a new power savings account.
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public PowerSavingAccount(int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(customerNumber, amount, createdOn);
        this.accountType = "PowerSavingAccount";
    }

    /**
     * Constructs a new debt account. This constructor is used specifically when files are being read.
     * @param accountNumber the account number of this account
     * @param customerNumber the customer number of the customer who owns this account
     * @param amount the display balance of this account
     * @param createdOn  the date and time this account was created on
     */
    public PowerSavingAccount(int accountNumber, int customerNumber, BigDecimal amount, LocalDateTime createdOn) {
        super(accountNumber, customerNumber, amount, createdOn);
        this.accountType = "PowerSavingAccount";
    }

    private double getMonthlyInterestRate() {
        if (this.balance.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            monthlyInterestRate = 0.001;
        } else if (this.balance.compareTo(BigDecimal.valueOf(3000)) <= 0) {
            monthlyInterestRate = 0.002;
        } else if (this.balance.compareTo(BigDecimal.valueOf(5000)) <= 0) {
            monthlyInterestRate = 0.003;
        } else if (this.balance.compareTo(BigDecimal.valueOf(7000)) <= 0) {
            monthlyInterestRate = 0.004;
        } else {
            monthlyInterestRate = 0.005;
        }
        return monthlyInterestRate;
    }

    @Override
    protected void addInterest() {
        BigDecimal currentBalance = this.getBalance();
        BigDecimal interestRate = BigDecimal.valueOf(this.getMonthlyInterestRate());
        BigDecimal interest = currentBalance.multiply(interestRate);
        this.setBalance(currentBalance.add(interest).setScale(2, RoundingMode.HALF_UP));
    }
}
