package atmMVC.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * An ATM that accesses a bank.
 */
public class ATM {
    /**
     * The number of $5 CAD bills this ATM contains
     */
    static int billOf5 = 100;
    /**
     * The number of $10 CAD bills this ATM contains
     */
    static int billOf10 = 100;
    /**
     * The number of $20 CAD bills this ATM contains
     */
    static int billOf20 = 100;
    /**
     * The number of $50 CAD bills this ATM contains
     */
    static int billOf50 = 100;
    /**
     * The number of $5 USD bills this ATM contains
     */
    static int billOf5USD = 100;
    /**
     * The number of $10 USD bills this ATM contains
     */
    static int billOf10USD = 100;
    /**
     * The number of $20 USD bills this ATM contains
     */
    static int billOf20USD = 100;
    /**
     * The number of $50 USD bills this ATM contains
     */
    static int billOf50USD = 100;

    /**
     * The bank the ATM accesses
     */
    private static Bank theBank;

    /**
     * Constructs a new ATM.
     */
    public ATM() {
        theBank = new Bank();
    }

    /**
     * Gets the total amount of money stored in this ATM.
     */
    public static BigDecimal getATMMoney() {
        return BigDecimal.valueOf(billOf5 * 5 + billOf10 * 10 + billOf20 * 20 + billOf50 * 50);
    }

    public static BigDecimal getATMMoneyUSD() {
        return BigDecimal.valueOf(billOf5USD * 5 + billOf10USD * 10 + billOf20USD * 20 + billOf50USD * 50);
    }

    public static int getBillOf5() {
        return billOf5;
    }

    public static int getBillOf10() {
        return billOf10;
    }

    public static int getBillOf20() {
        return billOf20;
    }

    public static int getBillOf50() {
        return billOf50;
    }

    public static int getBillOf5USD() {
        return billOf5USD;
    }

    public static int getBillOf10USD() {
        return billOf10USD;
    }

    public static int getBillOf20USD() {
        return billOf20USD;
    }

    public static int getBillOf50USD() {
        return billOf50USD;
    }

    public Bank getTheBank() {
        return theBank;
    }
}