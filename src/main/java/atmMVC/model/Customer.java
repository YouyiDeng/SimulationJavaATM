package atmMVC.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * A bank customer with a chequing and a savings account.
 */
public class Customer extends IdentifiablePerson {
    // customer identifier
    static int nextCustomerNumber = 1;
    private int customerNumber;
    ChequingAccount primaryChequingAccount;
    private ArrayList<Account> accounts;
    private ArrayList<Account> jointAccounts;

    /**
     * Constructs a customer with a given username and password.
     *
     * @param username the username for this customer
     * @param password the password for this customer
     * @param sinNumber the SIN number for this customer
     */
    public Customer(String username, String password, int sinNumber) {
        super(username, password, sinNumber);
        this.customerNumber = nextCustomerNumber;
        nextCustomerNumber++;
        accounts = new ArrayList<>();
        jointAccounts = new ArrayList<>();
        // load from file
        try {
            readAccounts();
            updateNextAccountNumber();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructs a customer with a given customerNumber, username and password.
     *
     * @param customerNumber the customer identifier
     * @param username       the username for this customer
     * @param password       the password for this customer
     * @param sinNumber the SIN number for this customer
     */
    public Customer(int customerNumber, String username, String password, int sinNumber) {
        super(username, password, sinNumber);
        this.customerNumber = customerNumber;
        accounts = new ArrayList<>();
        jointAccounts = new ArrayList<>();
        // load from file
        try {
            readAccounts();
            updateNextAccountNumber();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reads any accounts the customer may have using the accountFile.
     */
    private void readAccounts() {
        File acctFile = new File(Config.accountFile);
        if (acctFile.exists() && acctFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(acctFile))) {
                Scanner in = new Scanner(reader);
                while (in.hasNext()) {
                    int accountNumber = in.nextInt();
                    int customerNumber = in.nextInt();
                    BigDecimal balance = in.nextBigDecimal();
                    String strCreatedOn = in.next();
                    LocalDateTime createdOn = LocalDateTime.parse(strCreatedOn);
                    String accountType = in.next();
                    boolean isPrimary = in.nextBoolean();
                    int recentTrxID = in.nextInt();
                    boolean joint = in.nextBoolean();
                    int secondCustomerNumber = in.nextInt();
                    Account a = null;
                    if (this.customerNumber == customerNumber) {
                        a = makeAccount(accountNumber, customerNumber, balance, createdOn, accountType,  isPrimary,
                                recentTrxID, joint);
                        if (a != null)
                            accounts.add(a);
                    }
                    else if(this.customerNumber == secondCustomerNumber && joint) {
                        a = makeAccount(accountNumber, customerNumber, balance, createdOn, accountType,  isPrimary,
                                recentTrxID, joint);
                        if (a != null) {
                            jointAccounts.add(a);
                        }
                    }
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Account makeAccount(int accountNumber, int customerNumber, BigDecimal balance, LocalDateTime createdOn,
                                String accountType,  boolean isPrimary, int recentTrxID, boolean joint) {
        Account a = null;
        if (accountType.equalsIgnoreCase("CreditCardAccount"))
            a = new CreditCardAccount(accountNumber, customerNumber, balance, createdOn);
        else if (accountType.equalsIgnoreCase("LineOfCreditAccount"))
            a = new LineOfCreditAccount(accountNumber, customerNumber, balance, createdOn);
        else if (accountType.equalsIgnoreCase("ChequingAccount")) {
            a = new ChequingAccount(accountNumber, customerNumber, balance, createdOn, isPrimary);
            if(isPrimary)
                this.primaryChequingAccount = (ChequingAccount)a;
        }
        else if (accountType.equalsIgnoreCase("SavingAccount"))
            a = new SavingAccount(accountNumber, customerNumber, balance, createdOn);
        else if (accountType.equalsIgnoreCase("PowerSavingAccount"))
            a = new PowerSavingAccount(accountNumber, customerNumber, balance, createdOn);

        if (a != null) {
            a.recentTrxID = recentTrxID;
            if(joint) {
                a.setJoint(true);
                a.setSecondCustomer(this);
            }
        }
        return a;
    }

    private void updateNextAccountNumber() {
        int lastNumber = 0;
        for (Account a : accounts) {
            if (a.getAccountNumber() > lastNumber)
                lastNumber = a.getAccountNumber();
        }
        // account file has accounts for all customers
        // next account number needs to be the largest
        if (Account.nextAccountNumber < (lastNumber + 1))
            Account.nextAccountNumber = lastNumber + 1;
    }

    /**
     * @return the customers customer number
     */
    public int getCustomerNumber() {
        return this.customerNumber;
    }

    /**
     * Tests if this customer matches a customer number
     * and PIN.
     *
     * @param aNumber a customer number
     * @return true if the customer number and PIN match
     */
    public boolean match(int aNumber) {
        return this.customerNumber == aNumber;
    }

    /**
     * Tests if this customer matches a customer number
     * and PIN.
     *
     * @param username the username for this customer
     * @param password the password for this customer
     * @return true if the customer number and PIN match
     */
    public boolean match(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

    /**
     * Finds the customer account corresponding to the inputted account number
     *
     * @param accountNumber the account number
     * @return Account if customer has an account with accountNumber
     */
    public Account findAccount(int accountNumber) {
        for(Account a: accounts){
            if(a.getAccountNumber() == accountNumber)
                return a;
        }
        for(Account a: jointAccounts)
        {
            if(a.getAccountNumber() == accountNumber)
                return a;
        }
        return null;
    }

    /**
     * Creates a request for the creation of a new account
     * @param accountType the account type of the new account
     */
    public String requestNewAccount(String accountType) {
        File arFile = new File(Config.accountRequestFile);
        boolean hasAccess = true;
        if (!arFile.exists()) {
            try {
                hasAccess = arFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (arFile.exists() && arFile.isFile() || hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arFile, true))) {
                String line = this.getCustomerNumber() + "\t" +
                        accountType + "\t" +
                        LocalDateTime.now() + "\n";
                bw.write(line);
                return "Your request for " + accountType + " has been sent to Bank Manager.";
            } catch (IOException e) {
                return e.getMessage();
            }
        }
        return Config.accountRequestFile + " cannot be accessed.";
    }

    /**
     * Returns a user-friendly representation of a Customer object
     */
    @Override
    public String toString() {
        if (accounts.size() == 0)
            return "You don't have any account yet.";
        StringBuilder sb = new StringBuilder();
        sb.append("The following is your account summary:");
        sb.append("\n");
        sb.append("AccountNumber");
        sb.append("\t");
        sb.append("AccountType");
        sb.append("\t\t\t\t");
        sb.append("CreatedOn");
        sb.append("\t\t\t\t\t");
        sb.append("Balance");
        sb.append("\t\t\t\t");
        sb.append("Primary");
        sb.append("\n");

        for (Account a: accounts) {
            sb.append(a.accountNumber);
            sb.append("\t\t\t\t");
            sb.append(a.accountType);
            if(a instanceof AssetAccount) {
                sb.append("\t\t\t");
            }
            else {
                sb.append("\t\t");
            }
            sb.append(a.createdOn);
            sb.append("\t\t");
            sb.append(a.getBalance());
            sb.append("\t\t\t\t");
            if(a instanceof ChequingAccount)
                sb.append(((ChequingAccount)a).getPrimary());
            sb.append("\n");
            Transaction transaction = BankManager.findTransaction(a.recentTrxID);
            if(transaction != null) {
                sb.append("Recent Transaction is: \n");
                sb.append(transaction.toString());
                sb.append("\n");
            }
        }
        sb.append("Your net total is ");
        sb.append(getTotalBalance().setScale(2, RoundingMode.HALF_UP).toString());
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Gets the total balance of all accounts and joint accounts this customer owns
     * @return the total balance
     */
    public BigDecimal getTotalBalance() {
        return getMyAccountsBalance(accounts).add(getMyAccountsBalance(jointAccounts));
    }
    private BigDecimal getMyAccountsBalance(ArrayList<Account> myAccounts) {
        BigDecimal netBalance = BigDecimal.valueOf(0);
        for (Account a: myAccounts) {
            if (a instanceof AssetAccount) {
                netBalance = netBalance.add(a.getBalance());
            } else {
                netBalance = netBalance.subtract(a.getBalance());
            }
        }
        return netBalance;
    }

    ArrayList<Account> getAccounts() {
        return accounts;
    }

    /**
     * Returns an observable arrayList of all AccountsForFX (both joint and personal) that the customer has.
     *
     */
    public ObservableList<AccountForFX> getAccountsFX() {
        return getAccountsFX(true);
    }

    public ObservableList<AccountForFX> getAccountsFX(boolean includeJointAccounts) {
        ObservableList<AccountForFX> accountsFX = FXCollections.observableArrayList();
        ArrayList<Account> combined = new ArrayList<>();
        combined.addAll(accounts);
        if(includeJointAccounts)
            combined.addAll(jointAccounts);

        if(combined.size() > 0) {
            for(Account a: combined) {
                int accountNumber = a.accountNumber;
                String accountType = a.accountType;
                BigDecimal balance = a.getBalance();
                LocalDateTime accountOpenDate = a.createdOn;
                boolean primary = false;
                if(a instanceof ChequingAccount)
                    primary = ((ChequingAccount)a).getPrimary();
                boolean joint = a.getJoint();
                accountsFX.add(new AccountForFX(accountNumber, accountType, balance, accountOpenDate, primary, joint));
            }
            Comparator<AccountForFX> comparator = Comparator.comparingInt(AccountForFX::getAccountNumber);
            FXCollections.sort(accountsFX, comparator);
        }

        return accountsFX;
    }
}
