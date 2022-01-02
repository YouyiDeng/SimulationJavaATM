package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transfers money in to this account from another account
 */

public class TransferIn extends Transaction {
    Account account2;

    /**
     * Constructs a new transfer in.
     * @param a the account that is being transferred in to
     * @param amount the amount that is being transferred
     * @param trxDateTime the time the transfer is made
     * @param fromAccount the account that the transfer occurs from
     */
    public TransferIn(Account a, BigDecimal amount, LocalDateTime trxDateTime, Account fromAccount) {
        super(a, amount, trxDateTime);
        this.account2 = fromAccount;
        this.trxType = "TransferIn";
        this.undoable = true;
    }

    /**
     * Constructs a new transfer in. This constructor is used specifically
     * when files are being read.
     * @param trxID the identification number of this particular transfer
     * @param c the customer that makes the transfer
     * @param a the account that is being transferred in to
     * @param amount the amount that is being transferred
     * @param trxDateTime the time the transfer is made
     * @param fromAccount the account that the transfer occurs from
     */
    public TransferIn(int trxID, Customer c, Account a, BigDecimal amount, LocalDateTime trxDateTime,
                      Account fromAccount) {
        super(trxID, a, amount, trxDateTime);
        this.account2 = fromAccount;
        this.trxType = "TransferIn";
        this.undoable = true;
    }

    /**
     * Carries out this transfer from another account to this account
     * @return whether this transfer has been carried out successfully
     */
    public boolean doTransaction() {
        if(!(this.account2 instanceof CreditCardAccount) && this.account2.isAbleToTakeOutMoney(this.trxAmount)) {
            this.account.addAmount(this.trxAmount);
            this.account2.subtractAmount(this.trxAmount);

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
