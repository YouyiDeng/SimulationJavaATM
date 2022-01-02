package atmMVC.model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * A transaction.
 */
public abstract class Transaction {
    static int nextTrxID = 1;
    static Transaction mostRecentTrx;
    protected int trxID;
    protected Account account;
    protected LocalDateTime trxDateTime;
    protected BigDecimal trxAmount;
    protected String trxType;
    protected boolean undoable;
    protected boolean isReversingEntry;

    /**
     * Constructs a new transaction.
     * @param a the account that the transaction is occurring from
     * @param amount the amount of the transaction
     * @param trxDateTime the time the transaction is made
     */
    public Transaction(Account a, BigDecimal amount, LocalDateTime trxDateTime) {
        this.account = a;
        this.trxAmount = amount;
        this.trxDateTime = trxDateTime;
        this.trxID = nextTrxID++;
    }

    /**
     * Constructs a new transaction. This constructor is used specifically when files are being read.
     * @param trxID the identification number of this particular transaction
     * @param a the account that the transaction is occurring from
     * @param amount the amount of the transaction
     * @param trxDateTime the time the transaction is made
     */
    public Transaction(int trxID, Account a, BigDecimal amount, LocalDateTime trxDateTime) {
        this.account = a;
        this.trxAmount = amount;
        this.trxDateTime = trxDateTime;
        this.trxID = trxID;
    }
    /**
     * Carries out this transaction
     * @return whether this transaction is successfully carried out
     */
    abstract boolean doTransaction();

    /**
     * Reverses a particular transaction
     * @param aTrx the transaction to reverse
     * @return whether the transaction is successfully reversed
     */
    static boolean unDoTransaction(Transaction aTrx) {
        boolean result = false;
        if (aTrx.undoable) {
            // reverse amount sign
            aTrx.trxAmount = aTrx.trxAmount.negate();
            aTrx.trxDateTime = LocalDateTime.now();
            aTrx.isReversingEntry = true;
            result = aTrx.doTransaction();
        }
        return result;
    }

    protected void addTrxToFile(Transaction trx) {
        File tFile = new File(Config.trxFile);
        boolean hasAccess = true;
        if (!tFile.exists()) {
            try {
                hasAccess = tFile.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (tFile.exists() && tFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tFile, true))) {
                StringBuilder sb = new StringBuilder();
                sb.append(trx.trxID);
                sb.append("\t");
                sb.append(trx.account.customerNumber);
                sb.append("\t");
                sb.append(trx.trxType);
                sb.append("\t");
                sb.append(trx.trxAmount.setScale(2, RoundingMode.HALF_UP).toString());
                sb.append("\t");
                sb.append(trx.account.accountNumber);
                sb.append("\t");
                sb.append(trx.trxDateTime);
                sb.append("\t");
                if (trx instanceof TransferIn)
                    sb.append(((TransferIn) trx).account2.accountNumber);
                else if (trx instanceof TransferOut) {
                    sb.append(((TransferOut) trx).account2.accountNumber);
                } else if (trx instanceof TransferToOther) {
                    sb.append(((TransferToOther) trx).customer2.getCustomerNumber());
                } else if (trx instanceof PayBill) {
                    sb.append(((PayBill) trx).payee);
                } else
                    sb.append("0");  // dummy integer for deposit and withdraw as only one account is involved
                sb.append("\t");
                sb.append(trx.undoable);
                sb.append("\t");
                // Reversed or not
                sb.append(trx.isReversingEntry);
                sb.append("\n");
                bw.write(sb.toString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected static void updateAccountFile(Account a) {
        File acctFile = new File(Config.accountFile);
        if (acctFile.exists() && acctFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(acctFile))) {
                String line;
                StringBuilder inputBuffer = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    String[] accountFieldsArray = line.split("\t");
                    if (accountFieldsArray[0].equalsIgnoreCase(Integer.toString(a.accountNumber))) {
                        // balance field
                        accountFieldsArray[2] = a.getBalance().setScale(2, RoundingMode.HALF_UP).toString();
                        // recent transaction ID field
                        accountFieldsArray[6] = String.valueOf(a.recentTrxID);
                        line = String.join("\t", accountFieldsArray);
                    }
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
                String inputStr = inputBuffer.toString();
                reader.close();

                // write the new String with the updated account OVER the same account file
                FileOutputStream fileOut = new FileOutputStream(acctFile);
                fileOut.write(inputStr.getBytes());
                fileOut.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Returns a user-friendly representation of this Transaction object
     */
    @Override
    public String toString() {
        String info = "TrxID: " + this.trxID + " TrxType: " + this.trxType + " TrxAmount: " +
                this.trxAmount.setScale(2, RoundingMode.HALF_UP).toString() +
                " AccountNumber: " + this.account.accountNumber + " TrxDate: " + this.trxDateTime;

        if (this instanceof TransferIn)
            info += " Transferred from account: " + ((TransferIn) this).account2.accountNumber;
        else if (this instanceof TransferOut)
            info += " Transferred out to account: " + ((TransferOut) this).account2.accountNumber;
        else if (this instanceof TransferToOther)
            info += " Transferred out to customer: " + ((TransferToOther) this).customer2.getCustomerNumber();
        else if (this instanceof PayBill)
            info += " Pay bill to: " + ((PayBill) this).payee;

        info += " ReversingEntry?: " + this.isReversingEntry + "\n";
        return info;
    }
    public static Transaction getMostRecentTrx() {
        return mostRecentTrx;
    }
    public int getTrxID() {
        return trxID;
    }
    public Account getAccount() {
        return account;
    }
    public LocalDateTime getTrxDateTime() {
        return trxDateTime;
    }
    public BigDecimal getTrxAmount() {
        return trxAmount;
    }
    public String getTrxType() {
        return trxType;
    }
    public boolean getUndoable() {
        return undoable;
    }
    public boolean getIsReversingEntry() {
        return isReversingEntry;
    }

    /**
     * If the transaction is a transfer in/out, returns the account number of the account being transferred in/out of .
     * If the transaction is a transfer to other/pay bill, returns the customer number or the payee number respectively
     * Otherwise returns 0.
     */
    public int getSecondID() {
        if (this instanceof TransferIn)
            return ((TransferIn) this).account2.accountNumber;
        else if (this instanceof TransferOut) {
            return ((TransferOut) this).account2.accountNumber;
        } else if (this instanceof TransferToOther) {
            return ((TransferToOther) this).customer2.getCustomerNumber();
        } else if (this instanceof PayBill) {
            return ((PayBill) this).payee;
        } else
            return 0;  // dummy integer
    }
}
