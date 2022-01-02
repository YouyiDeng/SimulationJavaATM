package atmMVC.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Withdraws cash from the ATM.
 */

public class Withdrawal extends Transaction {

    private String currency;
    private BigDecimal amountForeign;

    /**
     * Constructs a new withdrawal.
     * @param a the account that is being used in the withdrawal
     * @param amount the amount that is being withdrawn
     * @param trxDateTime the time the withdrawal is made
     */
    public Withdrawal(Account a, BigDecimal amount, LocalDateTime trxDateTime) {
        super(a, amount, trxDateTime);
        this.trxType = "Withdrawal";
        this.undoable = true;
    }

    /**
     * Constructs a new withdrawal of foreign currency from the ATM
     * @param a the account that is being used in the withdrawal
     * @param amountCAD the amount of CAD currency being withdrawn from account a
     * @param amountForeign the amount of foreign currency being withdrawn
     * @param trxDateTime the time the withdrawal is made
     * @param foreignCurrency the 3 character String representing the foreign currency (ie. USD)
     */
    public Withdrawal(Account a, BigDecimal amountCAD, BigDecimal amountForeign, LocalDateTime trxDateTime,
                      String foreignCurrency) {
        super(a, amountCAD, trxDateTime);
        this.trxType = "Withdrawal";
        this.amountForeign = amountForeign;
        this.currency = foreignCurrency;
        this.undoable = false;
    }

    /**
     * Constructs a new withdrawal. This constructor is used specifically when files are being read.
     * @param trxID the identification number of this particular withdrawal
     * @param c the customer that makes the withdrawal
     * @param a the account that is being used in the withdrawal
     * @param amount the amount that is being withdrawn
     * @param trxDateTime the time the withdrawal is made
     */
    public Withdrawal(int trxID, Customer c, Account a, BigDecimal amount, LocalDateTime trxDateTime) {
        super(trxID, a, amount, trxDateTime);
        this.trxType = "Withdrawal";
        this.undoable = true;
    }

    public String getCurrency() { return this.currency; }
    public BigDecimal getAmountForeign() { return this.amountForeign; }

    /**
     * Carries out the withdrawal of cash from the ATM.
     * @return whether the withdrawal is carried out successfully
     */
    public boolean doTransaction() {
        if (this.currency == null) {
            if (isMultipleOf5(this.trxAmount) && atmHasEnoughMoney(this.trxAmount) &&
                    this.account.isAbleToTakeOutMoney(this.trxAmount)) {
                this.account.subtractAmount(this.trxAmount);
                addTrxToFile(this);
                this.account.recentTrxID = this.trxID;
                updateAccountFile(this.account);
                mostRecentTrx = this;
                updateATMMoney();
                // Bank reloads
                Bank.reloadBank();
                return true;
            }
            return false;
        } else if (this.currency.equals("USD")) {
            if (isMultipleOf5(this.amountForeign) && atmHasEnoughMoney(this.amountForeign) &&
                    this.account.isAbleToTakeOutMoney(this.trxAmount)) {
                this.account.subtractAmount(this.trxAmount.setScale(2, BigDecimal.ROUND_FLOOR));
                addTrxToFile(this);
                this.account.recentTrxID = this.trxID;
                updateAccountFile(this.account);
                mostRecentTrx = this;
                updateATMMoneyUSD();
                // Bank reloads
                Bank.reloadBank();
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Checks whether an entered amount is a multiple of 5
     * @param money the amount that is being checked
     * @return whether an amount is a multiple of 5
     */
    public boolean isMultipleOf5(BigDecimal money) {
        if (this.currency == null) {
            if(money == null || money.remainder(BigDecimal.valueOf(5)).equals(BigDecimal.valueOf(0)))
                return true;
        } else {
            if (money.remainder(BigDecimal.valueOf(5)).intValue() == 0) {
                return true;
            }
        }
        System.out.println("The amount has to be the multiple of $5.");
        return false;
    }

    /**
     * Checks whether the ATM contains a certain amount of money
     * @param money the amount that is being checked
     * @return whether the ATM contains a certain amount of money
     */
    public boolean atmHasEnoughMoney(BigDecimal money) {
        if(money == null || money.compareTo(ATM.getATMMoney()) <= 0 && this.currency == null ||
                money.compareTo(ATM.getATMMoneyUSD()) <= 0 && this.currency.equals("USD"))
            return true;
        System.out.println("Withdraw amount is greater than ATM has.");
        return false;
    }

    private void updateATMMoney() {
        BigDecimal[] divideBy50 = this.trxAmount.divideAndRemainder(BigDecimal.valueOf(50));
        if (divideBy50[0].compareTo(BigDecimal.valueOf(ATM.getBillOf50())) > 0) {
            divideBy50[0] = BigDecimal.valueOf(ATM.getBillOf50());
            divideBy50[1] = this.trxAmount.subtract(BigDecimal.valueOf(ATM.getBillOf50() * 50));
        }

        BigDecimal[] divideBy20 = divideBy50[1].divideAndRemainder(BigDecimal.valueOf(20));
        if (divideBy20[0].compareTo(BigDecimal.valueOf(ATM.getBillOf20())) > 0) {
            divideBy20[0] = BigDecimal.valueOf(ATM.getBillOf20());
            divideBy20[1] = divideBy50[1].subtract(BigDecimal.valueOf(ATM.getBillOf20() * 20));
        }

        BigDecimal[] divideBy10 = divideBy20[1].divideAndRemainder(BigDecimal.valueOf(10));
        if (divideBy10[0].compareTo(BigDecimal.valueOf(ATM.getBillOf10())) > 0) {
            divideBy10[0] = BigDecimal.valueOf(ATM.getBillOf10());
            divideBy10[1] = divideBy20[1].subtract(BigDecimal.valueOf(ATM.getBillOf10() * 10));
        }
        // should be dividable by 5, anyway
        BigDecimal[] divideBy5 = divideBy10[1].divideAndRemainder(BigDecimal.valueOf(5));

        ATM.billOf50 -= divideBy50[0].intValue();
        ATM.billOf20 -= divideBy20[0].intValue();
        ATM.billOf10 -= divideBy10[0].intValue();
        ATM.billOf5 -= divideBy5[0].intValue();

        // send alert to manager
        if (ATM.getBillOf50() < 20 || ATM.getBillOf20() < 20 || ATM.getBillOf10() < 20 || ATM.getBillOf5() < 20) {
            sendAlertToManager();
        }
    }

    private void updateATMMoneyUSD() {
        BigDecimal[] divideBy50 = this.amountForeign.divideAndRemainder(BigDecimal.valueOf(50));
        if (divideBy50[0].compareTo(BigDecimal.valueOf(ATM.getBillOf50USD())) > 0) {
            divideBy50[0] = BigDecimal.valueOf(ATM.getBillOf50USD());
            divideBy50[1] = this.amountForeign.subtract(BigDecimal.valueOf(ATM.getBillOf50USD() * 50));
        }

        BigDecimal[] divideBy20 = divideBy50[1].divideAndRemainder(BigDecimal.valueOf(20));
        if (divideBy20[0].compareTo(BigDecimal.valueOf(ATM.getBillOf20USD())) > 0) {
            divideBy20[0] = BigDecimal.valueOf(ATM.getBillOf20USD());
            divideBy20[1] = divideBy50[1].subtract(BigDecimal.valueOf(ATM.getBillOf20USD() * 20));
        }

        BigDecimal[] divideBy10 = divideBy20[1].divideAndRemainder(BigDecimal.valueOf(10));
        if (divideBy10[0].compareTo(BigDecimal.valueOf(ATM.getBillOf10USD())) > 0) {
            divideBy10[0] = BigDecimal.valueOf(ATM.getBillOf10USD());
            divideBy10[1] = divideBy20[1].subtract(BigDecimal.valueOf(ATM.getBillOf10USD() * 10));
        }
        // should be dividable by 5, anyway
        BigDecimal[] divideBy5 = divideBy10[1].divideAndRemainder(BigDecimal.valueOf(5));

        ATM.billOf50USD -= divideBy50[0].intValue();
        ATM.billOf20USD -= divideBy20[0].intValue();
        ATM.billOf10USD -= divideBy10[0].intValue();
        ATM.billOf5USD -= divideBy5[0].intValue();

        // send alert to manager
        if (ATM.getBillOf50USD() < 20 || ATM.getBillOf20USD() < 20 || ATM.getBillOf10USD() < 20 ||
                ATM.getBillOf5USD() < 20) {
            sendAlertToManager();
        }
    }

    private void sendAlertToManager() {
        File aFile = new File(Config.alertFile);
        boolean hasAccess = true;
        if (!aFile.exists()) {
            try {
                hasAccess = aFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (aFile.exists() && aFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(aFile, true))) {
                String alert = "Please add money to ATM.  Today is " + LocalDateTime.now() + "\n" +
                        "ATM has " + ATM.getBillOf50() + " $50 bills." + "\n" +
                        "ATM has " + ATM.getBillOf20() + " $20 bills." + "\n" +
                        "ATM has " + ATM.getBillOf10() + " $10 bills." + "\n" +
                        "ATM has " + ATM.getBillOf5() + " $5 bills." + "\n" +
                        "ATM has " + ATM.getBillOf50USD() + " $50 bills." + "\n" +
                        "ATM has " + ATM.getBillOf20USD() + " $20 bills." + "\n" +
                        "ATM has " + ATM.getBillOf10USD() + " $10 bills." + "\n" +
                        "ATM has " + ATM.getBillOf5USD() + " $5 bills." + "\n";

                bw.write(alert);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
