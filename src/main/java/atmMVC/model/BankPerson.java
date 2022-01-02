package atmMVC.model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A person who works at the bank.
 */

public class BankPerson extends IdentifiablePerson {
    static int nextEmployeeNumber = 1;
    protected int employeeNumber;
    protected String role;
    protected Customer mySelfAsCustomer;

    /**
     * Constructs a bank person with a given username and password.
     * @param username the username for this customer
     * @param password the password for this customer
     * @param sinNumber the SIN number for this customer
     */
    public BankPerson(String username, String password, int sinNumber) {
        super(username, password, sinNumber);
        this.employeeNumber = nextEmployeeNumber;
        nextEmployeeNumber ++;
        this.mySelfAsCustomer = findMySelf();
    }

    /**
     * Constructs a bank person with a given username and password.
     * @param  employeeNumber the customer identifier
     * @param username the username for this customer
     * @param password the password for this customer
     * @param sinNumber the SIN number for this customer
     */
    public BankPerson(int employeeNumber, String username, String password, int sinNumber) {
        super(username, password, sinNumber);
        this.employeeNumber = employeeNumber;
        this.mySelfAsCustomer = findMySelf();
    }

    /**
     * Helper method of BankPerson class constructors.
     * Gets and returns information of this BankPerson as an instance of Customer
     */
    private Customer findMySelf() {
        Customer c = null;
        File cFile = new File(Config.customerFile);
        if (cFile.exists() && cFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cFile))) {
                Scanner in = new Scanner(reader);
                while (in.hasNext()) {
                    int customerNumber = in.nextInt();
                    String username = in.next();
                    String password = in.next();
                    int sinNumber = in.nextInt();
                    if(this.sinNumber == sinNumber) {
                        c = new Customer(customerNumber, username, password, sinNumber);
                        return c;
                    }
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return c;
    }

    public Customer getMySelfAsCustomer() {
        return this.mySelfAsCustomer;
    }

    /**
     * Makes an account a joint account that can be accessed by 2 customers. This method is used when the account number
     * and customer number is passed in as parameters
     * @param accountNumber the account number of the account that is being made a joint account
     * @param customerNumber the new customer that is able to access this account
     */
    public boolean makeAccountJoint(int accountNumber, int customerNumber) {
        Account account = Bank.findAccount(accountNumber);
        Customer customer = Bank.findCustomer(customerNumber);
        if(account != null && customer != null)
            return makeAccountJoint(account, customer);
        else
            return false;
    }

    /**
     * Makes an account a joint account that can be accessed by 2 customers. This method is used when instances of
     * Account and Customer are passed in as parameters
     * @param account the account that is being made a joint account
     * @param customer the new customer that is able to access this account
     */
    private boolean makeAccountJoint(Account account, Customer customer) {
        if(account instanceof ChequingAccount || account instanceof SavingAccount) {
            if(account.getCustomerNumber() != customer.getCustomerNumber() && !account.getJoint()) {
                account.setJoint(true);
                account.setSecondCustomer(customer);
                updateAccountFileForJoint(account);
                Bank.reloadBank();
                return true;
            }
        }
        return false;
    }

    private void updateAccountFileForJoint(Account a) {
        File acctFile = new File(Config.accountFile);
        if (acctFile.exists() && acctFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(acctFile))) {
                String line;
                StringBuilder inputBuffer = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    String[] accountFieldsArray = line.split("\t");
                    if (accountFieldsArray[0].equalsIgnoreCase(Integer.toString(a.accountNumber))) {
                        // joint field
                        accountFieldsArray[7] = String.valueOf(a.getJoint());
                        // second customer number field
                        accountFieldsArray[8] = String.valueOf(a.getSecondCustomer().getCustomerNumber());

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

    protected void addNewAccountToFile(Account a) {
        File acctFile = new File(Config.accountFile);
        boolean hasAccess = true;
        if (!acctFile.exists()) {
            try {
                hasAccess = acctFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (acctFile.exists() && acctFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(acctFile, true))) {
                StringBuilder sb = new StringBuilder();
                sb.append(a.getAccountNumber());
                sb.append("\t");
                sb.append(a.customerNumber);
                sb.append("\t");
                sb.append(a.getBalance().setScale(2, RoundingMode.HALF_UP).toString());
                sb.append("\t");
                sb.append(a.createdOn);
                sb.append("\t");
                sb.append(a.accountType);
                sb.append("\t");
                if (a instanceof ChequingAccount)
                    sb.append(((ChequingAccount)a).getPrimary());
                else
                    sb.append(false);
                sb.append("\t");
                sb.append(a.recentTrxID);

                sb.append("\t");
                sb.append(a.getJoint());
                sb.append("\t");
                if(a.getSecondCustomer() != null)
                    sb.append(a.getSecondCustomer().getCustomerNumber());
                else
                    sb.append("0");

                sb.append("\n");
                bw.write(sb.toString());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Restocks ATM bills.
     * @param bills the number of bills to be restocked in order of $5, $10, $20, $50 in CAD, then in USD
     */
    public void addMoneyToATM(int[] bills) {
        ATM.billOf5 += bills[0];
        ATM.billOf10 += bills[1];
        ATM.billOf20 += bills[2];
        ATM.billOf50 += bills[3];
        ATM.billOf5USD += bills[4];
        ATM.billOf10USD += bills[5];
        ATM.billOf20USD += bills[6];
        ATM.billOf50USD += bills[7];
    }

    /**
     * Processes a customer deposit, if present, and changes the customer's account balance accordingly
     */
    public String processDepositFile() {
        ArrayList<Account> updatedAccounts = new ArrayList<>();
        File dFile = new File(Config.depositFile);
        if (dFile.exists() && dFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(dFile))) {
                Scanner in = new Scanner(reader);
                // fields in deposit file: account#, customer#, amount
                while (in.hasNext()) {
                    int accountNumber = in.nextInt();
                    int customerNumber = in.nextInt();
                    BigDecimal amount = in.nextBigDecimal();

                    Customer c = Bank.findCustomer(customerNumber);
                    if (c != null) {
                        Account a = c.findAccount(accountNumber);
                        if(a != null) {
                            updatedAccounts.add(a);
                            Deposit deposit = new Deposit(a, amount, LocalDateTime.now());
                            deposit.doTransaction();
                        }
                    }
                }
                in.close();
                reader.close();

                String inputStr = "";
                // wipe out deposit file
                FileOutputStream fileOut = new FileOutputStream(dFile);
                fileOut.write(inputStr.getBytes());
                fileOut.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if(updatedAccounts.size() > 0) {
            return updatedAccounts.size() + " deposits have been processed.";
        }
        else
            return "There are no deposits to process.";
    }

    /**
     * Finds a particular transaction a customer has made
     * @param transactionID the transaction ID of the transaction that is being searched for
     */
    public static Transaction findTransaction(int transactionID) {
        File tFile = new File(Config.trxFile);
        Transaction recentTransaction = null;
        Transaction transaction;
        if (tFile.exists() && tFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(tFile))) {
                Scanner in = new Scanner(reader);
                // transaction file field: trxID, customer#, trxType, trxAmount, accountNumber, trxDateTime,
                // accountNumber2/CustomerNumber2/payee, undoable, isReversingEntry
                while (in.hasNext()) {
                    transaction = null;
                    int trxID = in.nextInt();
                    int customerNumber = in.nextInt();
                    String trxType = in.next();
                    BigDecimal trxAmount = in.nextBigDecimal();
                    int accountNumber = in.nextInt();
                    LocalDateTime trxDateTime = LocalDateTime.parse(in.next());
                    int number2 = in.nextInt();
                    boolean undoable = in.nextBoolean();
                    boolean reversingEntry = in.nextBoolean();
                    if (trxID == transactionID) {
                        transaction = makeTransaction(trxID, trxType, trxAmount, trxDateTime, undoable, reversingEntry,
                                customerNumber, accountNumber, number2);
                        if(transaction != null) {
                            if(recentTransaction == null ||
                                    recentTransaction.trxDateTime.compareTo(transaction.trxDateTime) < 0)
                                recentTransaction = transaction;
                        }
                    }
                }
                in.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return recentTransaction;
    }

    private static Transaction makeTransaction(
            int trxID, String trxType, BigDecimal trxAmount, LocalDateTime trxDateTime, boolean undoable,
            boolean reversingEntry, int customerNumber, int accountNumber, int number2) {

        Transaction transaction = null;
        Customer c = Bank.findCustomer(customerNumber);
        if (c != null) {
            Account a = c.findAccount(accountNumber);
            if(a != null){
                if (trxType.equalsIgnoreCase("Deposit"))
                    transaction = new Deposit(trxID, c, a, trxAmount, trxDateTime);
                else if (trxType.equalsIgnoreCase("Withdrawal"))
                    transaction = new Withdrawal(trxID, c, a, trxAmount, trxDateTime);
                else if (trxType.equalsIgnoreCase("TransferIn")) {
                    Account a2 = c.findAccount(number2);
                    if(a2 != null)
                        transaction = new TransferIn(trxID, c, a, trxAmount, trxDateTime, a2);
                }
                else if (trxType.equalsIgnoreCase("TransferOut")) {
                    Account a2 = c.findAccount(number2);
                    if(a2 != null)
                        transaction = new TransferOut(trxID, c, a, trxAmount, trxDateTime, a2);
                }
                else if (trxType.equalsIgnoreCase("TransferToOther")) {
                    Customer c2 = Bank.findCustomer(number2);
                    if(c2 != null)
                        transaction = new TransferToOther(trxID, c, a, trxAmount, trxDateTime, c2);
                }
                else if (trxType.equalsIgnoreCase("PayBill"))
                    transaction = new PayBill(trxID, c, a, trxAmount, trxDateTime, number2);
            }
            if(transaction != null) {
                transaction.undoable = undoable;
                transaction.isReversingEntry = reversingEntry;
            }
        }
        return transaction;
    }
}
