package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transfers money from one customer's account to another customer's primary chequing account.
 */

public class TransferToOther extends Transaction {


    /** The customer that receives the transfer */
    Customer customer2;

    /**
     * Constructs a new transfer to another user
     * @param a the account that transfers money to another user
     * @param amount the amount that is being transferred
     * @param trxDateTime the time the transfer is made
     * @param toCustomer the customer that receives the transfer
     */
    public TransferToOther(Account a, BigDecimal amount, LocalDateTime trxDateTime, Customer toCustomer) {
        super(a, amount, trxDateTime);
        this.customer2 = toCustomer;
        this.trxType = "TransferToOther";
        this.undoable = true;
    }

    /**
     * Constructs a new transfer to another user's account. This constructor is used specifically
     * when files are being read.
     * @param trxID the identification number of this particular transaction
     * @param c the customer that makes the transfer
     * @param a the account that is being used to transfer money to another user
     * @param amount the amount that is being transferred
     * @param trxDateTime the time the transfer is made
     * @param toCustomer the customer that receives the transfer
     */
    public TransferToOther(int trxID, Customer c, Account a, BigDecimal amount,
                           LocalDateTime trxDateTime, Customer toCustomer){
        super(trxID, a, amount, trxDateTime);
        this.customer2 = toCustomer;
        this.trxType = "TransferToOther";
        this.undoable = true;
    }

    /**
     * Carries out this transfer to another customer's primary chequing account
     * @return whether this transfer to another customer's primary chequing account is successfully carried out
     */
    public boolean doTransaction() {
        if(!(this.account instanceof CreditCardAccount) &&
                this.account.isAbleToTakeOutMoney(this.trxAmount) &&
                otherCustomerHasPrimaryAccount(this.customer2)) {
            // in case this account is the same joint by second customer as his primary chequing
            //account there is no need to transfer money
            if(this.account.getAccountNumber() != customer2.primaryChequingAccount.getAccountNumber()) {
                this.account.subtractAmount(this.trxAmount);
                this.customer2.primaryChequingAccount.addAmount(this.trxAmount);
            }
            addTrxToFile(this);
            // update the account balance in the file
            this.account.recentTrxID = this.trxID;
            updateAccountFile(this.account);
            this.customer2.primaryChequingAccount.recentTrxID = this.trxID;
            updateAccountFile(this.customer2.primaryChequingAccount);
            mostRecentTrx = this;
            // Bank reloads
            Bank.reloadBank();
            return true;
        }
        return false;
    }
    private boolean otherCustomerHasPrimaryAccount(Customer customer) {
        if(customer.primaryChequingAccount == null) {
            System.out.println("The other customer doesn't have Chequing Account.  Cannot do transfer.");
            return false;
        }
        return true;
    }
}
