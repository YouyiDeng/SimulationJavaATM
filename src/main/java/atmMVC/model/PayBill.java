package atmMVC.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Transfers money from one customer's account to a non-user.
 */


public class PayBill extends Transaction {
    int payee;

    /**
     * Constructs a new bill payment to a non-users account.
     * @param a the account that is being used to make the payment
     * @param amount the amount that is being paid
     * @param trxDateTime the time the bill payment is made
     * @param payee the non-user that receives the bill payment
     */
    public PayBill(Account a, BigDecimal amount, LocalDateTime trxDateTime, int payee) {
        super(a, amount, trxDateTime);
        this.payee = payee;
        this.trxType = "PayBill";
        this.undoable = false;
    }

    /**
     * Constructs a new bill payment to a non-users account. This constructor is used specifically
     * when files are being read.
     * @param trxID the identification number of this particular transaction
     * @param c the customer that makes the payment
     * @param a the account that is being used to make the payment
     * @param amount the amount that is being paid
     * @param trxDateTime the time the bill payment is made
     * @param payee the non-user that receives the bill payment
     */
    public PayBill(int trxID, Customer c, Account a, BigDecimal amount, LocalDateTime trxDateTime,
                   int payee)
    {
        super(trxID, a, amount, trxDateTime);
        this.payee = payee;
        this.trxType = "PayBill";
        this.undoable = false;
    }

    /**
     * Carries out this bill payment
     * @return whether this bill payment is successfully carried out
     */
    public boolean doTransaction() {
        if(this.account.isAbleToTakeOutMoney(this.trxAmount)) {
            this.account.subtractAmount(this.trxAmount);

            addTrxToFile(this);
            this.account.recentTrxID = this.trxID;
            updateAccountFile(this.account);
            // write to payment file billPaymentFile
            writeToBillPaymentFile();
            mostRecentTrx = this;
            // Bank reloads
            Bank.reloadBank();
            return true;
        }
        return false;
    }
    private void writeToBillPaymentFile(){
        File bFile = new File(Config.billPaymentFile);
        boolean hasAccess = true;
        if (!bFile.exists()) {
            try {
                hasAccess = bFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (bFile.exists() && bFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(bFile, true))) {
                String line = this.trxID + "\t" +
                        this.trxType + "\t" +
                        this.account.accountNumber + "\t" +
                        this.trxAmount.setScale(2, RoundingMode.HALF_UP).toString() + "\t" +
                        this.trxDateTime + "\t" +
                        this.payee + "\n";
                bw.write(line);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
