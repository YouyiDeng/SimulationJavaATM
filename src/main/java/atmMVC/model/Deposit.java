package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Deposits cash or cheque into the ATM.
 */

public class Deposit extends Transaction {

    private String currency;
    private BigDecimal amountForeign;

    /**
     * Constructs a new deposit into the ATM
     * @param a the account that the deposit comes from
     * @param amount the amount that is being deposited
     * @param trxDateTime the time the deposit is made
     */
    public Deposit(Account a, BigDecimal amount, LocalDateTime trxDateTime) {
        super(a, amount, trxDateTime);
        this.trxType = "Deposit";
        this.undoable = true;
    }

    /**
     * Constructs a new deposit of foreign currency into the ATM
     * @param a the account that the deposit comes from
     * @param amountForeign the amount of foreign currency being deposited
     * @param amountCAD the amount of CAD currency that will be deposited into account a
     * @param trxDateTime the time the deposit is made
     * @param foreignCurrency the 3 character String representing the foreign currency (ie. USD)
     */
    public Deposit(Account a, BigDecimal amountForeign, BigDecimal amountCAD, LocalDateTime trxDateTime,
                   String foreignCurrency) {
        super(a, amountCAD, trxDateTime);
        this.trxType = "Deposit";
        this.amountForeign = amountForeign;
        this.currency = foreignCurrency;
        this.undoable = false;
    }

    /**
     * Constructs a new deposit into the ATM. This constructor is used specifically
     * when files are being read.
     * @param trxID the identification number of this particular deposit
     * @param c the customer that makes the deposit
     * @param a the account that the deposit is coming from
     * @param amount the amount that is being deposited
     * @param trxDateTime the time the deposit is made
     */
    public Deposit(int trxID, Customer c, Account a, BigDecimal amount, LocalDateTime trxDateTime) {
        super(trxID, a, amount, trxDateTime);
        this.trxType = "Deposit";
        this.undoable = true;
    }

    /**
     * Carries out the deposit
     * @return whether the deposit is successfully carried out
     */
    public boolean doTransaction() {
        this.account.addAmount(this.trxAmount.setScale(2, BigDecimal.ROUND_FLOOR));

        // add trx to file
        addTrxToFile(this);
        // update the account balance in the file
        this.account.recentTrxID = this.trxID;
        updateAccountFile(this.account);
        mostRecentTrx = this;
        // Bank reloads
        Bank.reloadBank();
        return true;
    }
}
