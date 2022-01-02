package atmMVC.model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A Bank Util that contains set of methods which allow a bank to function properly.
 */
public class BankUtil {
    /**
     * Adds interest to all customers' savings accounts.
     * @param savingAccounts an ArrayList of all savings accounts
     */
    static void addInterestToSaving(ArrayList<SavingAccount> savingAccounts) {
        for(SavingAccount savingAccount: savingAccounts) {
            savingAccount.addInterest();
            // update account file
            Transaction.updateAccountFile(savingAccount);
        }
    }

    /**
     * Reads the customer identifier, username and password
     * @param customers an ArrayList of all customers
     */
    static void readCustomers(ArrayList<Customer> customers) {
        File cFile = new File(Config.customerFile);
        if (cFile.exists() && cFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cFile))) {
                Scanner in = new Scanner(reader);
                while (in.hasNext()) {
                    int customerNumber = in.nextInt();
                    String username = in.next();
                    String password = in.next();
                    int sinNumber = in.nextInt();
                    Customer c = new Customer(customerNumber, username, password, sinNumber);
                    customers.add(c);
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Reads the bank worker identifier, username and password
     * @param workers an ArrayList of all workers
     */
    static void readWorkers(ArrayList<BankPerson> workers) {
        File wFile = new File(Config.workerFile);
        if (wFile.exists() && wFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(wFile))) {
                Scanner in = new Scanner(reader);
                while (in.hasNext()) {
                    int employeeNumber = in.nextInt();
                    String username = in.next();
                    String password = in.next();
                    String role = in.next();
                    int sinNumber = in.nextInt();
                    BankPerson w;
                    if (role.equalsIgnoreCase("Manager"))
                        w = new BankManager(employeeNumber, username, password, sinNumber);
                    else
                        w = new BankAdvisor(employeeNumber, username, password, sinNumber);

                    workers.add(w);
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Updates the customer number that the next created customer is assigned.
     * @param customers an ArrayList of all customers
     */
    static void updateNextCustomerNumber(ArrayList<Customer> customers) {
        int lastNumber = 0;
        for (Customer c : customers) {
            if (c.getCustomerNumber() > lastNumber)
                lastNumber = c.getCustomerNumber();
        }
        Customer.nextCustomerNumber = lastNumber + 1;
    }

    /**
     * Updates the employee number that the next created worker is assigned.
     * @param workers an ArrayList of all workers
     */
    static void updateNextEmployeeNumber(ArrayList<BankPerson> workers) {
        int lastNumber = 0;
        for (BankPerson w : workers) {
            if (w.employeeNumber > lastNumber)
                lastNumber = w.employeeNumber;
        }
        BankPerson.nextEmployeeNumber = lastNumber + 1;
    }

    /**
     * Updates the transaction ID that the next created transaction is assigned
     */
    static void updateNextTrxID() {
        int lastNumber = 0;
        ///
        File tFile = new File(Config.trxFile);
        if (tFile.exists() && tFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(tFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] trxFieldsArray = line.split("\t");
                    int trxId;
                    try {
                        trxId = Integer.parseInt(trxFieldsArray[0]);
                    } catch (Exception e1) {
                        trxId = 0;
                    }
                    if (trxId > lastNumber)
                        lastNumber = trxId;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        // account file has accounts for all customers
        // next account number needs to be the largest
        if (Transaction.nextTrxID < (lastNumber + 1))
            Transaction.nextTrxID = lastNumber + 1;
    }

    /**
     * Adds a new customer to the file that stores all customers' information
     * @param c the customer to be added
     */
    static void addNewCustomerToFile(Customer c) {
        File cFile = new File(Config.customerFile);
        boolean hasAccess = true;
        if (!cFile.exists()) {
            try {
                hasAccess = cFile.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (cFile.exists() && cFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(cFile, true))) {
                String line = c.getCustomerNumber() + "\t" +
                        c.username + "\t" + c.password + "\t" + c.sinNumber + "\n";
                bw.write(line);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Adds a new worker to the file that stores all workers' information
     * @param w the worker to be added
     */
    static void addNewWorkerToFile(BankPerson w) {
        File wFile = new File(Config.workerFile);
        boolean hasAccess = true;
        if (!wFile.exists()) {
            try {
                hasAccess = wFile.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (wFile.exists() && wFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(wFile, true))) {
                String line = w.employeeNumber + "\t" +
                        w.username + "\t" + w.password + "\t" + w.role + "\t" + w.sinNumber + "\n";
                bw.write(line);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
