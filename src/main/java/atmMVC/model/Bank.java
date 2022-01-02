package atmMVC.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A bank contains customers with bank accounts.
 */
public class Bank {
    private static ArrayList<Customer> customers;
    private static ArrayList<BankPerson> workers;

    /**
     * Constructs a bank.
     */
    public Bank() {
        reloadBank();
        // on first day of each month, calculate interest and update account balance
        if (LocalDateTime.now().getDayOfMonth() == 1)
            BankUtil.addInterestToSaving(getSavingAccounts());
    }

    /**
     * Constructs a bank.
     *
     * @param localDate LocalDate
     */
    public Bank(LocalDate localDate) {
        // on first day of each month, calculate interest and update account balance
        if (localDate.getDayOfMonth() == 1)
            BankUtil.addInterestToSaving(getSavingAccounts());
        reloadBank();
    }

    /**
     * Reloads the bank with information saved from previous runs of the program
     */
    static void reloadBank() {
        customers = new ArrayList<>();
        workers = new ArrayList<>();
        try {
            BankUtil.readCustomers(customers);
            BankUtil.readWorkers(workers);
            BankUtil.updateNextCustomerNumber(customers);
            BankUtil.updateNextEmployeeNumber(workers);
            BankUtil.updateNextTrxID();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a new customer to the bank.
     *
     * @param c the customer to add
     */
    public void addNewCustomer(Customer c) {
        customers.add(c);
        // save new customer to file
        BankUtil.addNewCustomerToFile(c);
    }

    /**
     * Adds a worker to the bank.
     *
     * @param w the worker to add
     */
    public void addNewWorker(BankPerson w) {
        workers.add(w);
        // save new worker to file
        BankUtil.addNewWorkerToFile(w);
    }

    /**
     * Finds a customer in the bank using a customer's username and password
     *
     * @param username the username for this customer
     * @param password the password for this customer
     * @return the matching customer, or null if no customer
     * matches
     */
    public Customer findCustomer(String username, String password) {
        for (Customer c : customers) {
            if (c.match(username, password))
                return c;
        }
        return null;
    }

    /**
     * Finds a customer in the bank using a customer's customer number
     * @param customerNumber the customer number for this customer
     * @return the matching customer, or null if no customer
     */
    public static Customer findCustomer(int customerNumber) {
        for (Customer c : customers) {
            if (c.match(customerNumber))
                return c;
        }
        return null;
    }

    /**
     * Finds an account in the bank
     * @param accountNumber the account number of the account being searched for
     * @return the matching account, or null if no such account exists
     */
    public static Account findAccount(int accountNumber) {
        for (Customer c : customers) {
            for(Account account: c.getAccounts()) {
                if(account.getAccountNumber() == accountNumber)
                    return account;
            }
        }
        return null;
    }

    /**
     * Checks whether a username has been taken
     * @param username the username being checked
     * @param isCustomer whether the username being checked is a customer's username
     * @return whether the username has been taken
     */
    public boolean userNameTaken(String username, boolean isCustomer) {
        if(isCustomer) {
            for (Customer c : customers) {
                if (c.username.equalsIgnoreCase(username))
                    return true;
            }
            return false;
        }
        else {
            for (BankPerson p: workers) {
                if (p.username.equalsIgnoreCase(username))
                    return true;
            }
            return false;
        }
    }

    /**
     * Checks whether a sin number has been taken
     * @param sinNumber the sin number being checked
     * @param isCustomer whether the sin number being checked is a customer's sin number
     * @return whether the sin number has been taken
     */
    public boolean sinNumberTaken(int sinNumber, boolean isCustomer) {
        if(isCustomer) {
            for (Customer c : customers) {
                if (c.sinNumber == sinNumber)
                    return true;
            }
            return false;
        }
        else {
            for (BankPerson p: workers) {
                if (p.sinNumber == sinNumber)
                    return true;
            }
            return false;
        }
    }

    /**
     * Finds a worker in the bank.
     *
     * @param username the username for this customer
     * @param password the password for this customer
     * @return the matching customer, or null if no customer
     * matches
     */
    public BankPerson findWorker(String username, String password) {
        for (BankPerson w : workers) {
            if (w.username.equalsIgnoreCase(username) && w.password.equals(password))
                return w;
        }
        return null;
    }

    /**
     * Gets an ArrayList of all saving accounts in this bank
     * @return a collection of all saving accounts
     */
    public ArrayList<SavingAccount> getSavingAccounts() {
        ArrayList<SavingAccount> savingAccounts = new ArrayList<>();
        for (Customer c : customers) {
            ArrayList<Account> accounts = c.getAccounts();
            for(Account account: accounts) {
                if(account instanceof SavingAccount)
                    savingAccounts.add((SavingAccount)account);
            }
        }
        return savingAccounts;
    }

    /**
     * Returns an observable arrayList of every account within the bank.
     *
     */
    public ObservableList<AccountForFX> getAllAccountsFX() {
        ObservableList<AccountForFX> allAccountsFX = FXCollections.observableArrayList();
        for(Customer customer: customers) {
            allAccountsFX.addAll(customer.getAccountsFX(false));
        }
        return allAccountsFX;
    }

    /**
     * Gets an ArrayList of all customer numbers of this bank
     * @return a collection of all customer numbers
     */
    public ArrayList<Integer> getAllCustomerNumbers() {
        ArrayList<Integer> customerNumbers = new ArrayList<>();
        for (Customer customer : customers)
            customerNumbers.add(new Integer(customer.getCustomerNumber()));
        return customerNumbers;
    }
}

