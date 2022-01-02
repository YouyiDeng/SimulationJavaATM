package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transfers money out of this account to another account of the same customer
 */

public class TransferOut extends Transaction {
    Account account2;

    /**
     * Constructs a new transfer out.
     * @param a the account that is being transferred out of
     * @param amount the amount that is being transferred
     * @param trxDateTime the time the transfer is made
     * @param toAccount the account that is being transferred in to
     */
    public TransferOut(Account a, BigDecimal amount, LocalDateTime trxDateTime, Account toAccount) {
        super(a, amount, trxDateTime);
        this.account2 = toAccount;
        this.trxType = "TransferOut";
        this.undoable = true;
    }

    /**
     * Constructs a new transfer out. This constructor is used specifically when files are being read.
     * @param trxID the identification number of this particular transfer
     * @param c the customer that makes the transfer
     * @param a the account that is being transferred out of
     * @param amount the amount that is being transferred
     * @param trxDateTime the time the transfer is made
     * @param toAccount the account that is being transferred in to
     */
    public TransferOut(int trxID, Customer c, Account a, BigDecimal amount, LocalDateTime trxDateTime,
                       Account toAccount) {
        super(trxID, a, amount, trxDateTime);
        this.account2 = toAccount;
        this.trxType = "TransferOut";
        this.undoable = true;
    }

    /**
     * Carries out this transfer from this account to another account of the same customer
     * @return whether this transfer has been carried out successfully
     */
    public boolean doTransaction() {
        if(!(this.account instanceof CreditCardAccount) && this.account.isAbleToTakeOutMoney(this.trxAmount)) {
            this.account.subtractAmount(this.trxAmount);
            this.account2.addAmount(this.trxAmount);

            addTrxToFile(this);
            // update the account balance in the file
            this.account.recentTrxID = this.trxID;
            this.account2.recentTrxID = this.trxID;
            updateAccountFile(this.account);
            updateAccountFile(this.account2);
            mostRecentTrx = this;
            // Bank reloads
            Bank.reloadBank();
            return true;
        }
        return false;
    }

}
