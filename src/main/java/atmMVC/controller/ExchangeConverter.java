package atmMVC.controller;

import java.util.Random;

/**
 * Converts between 9 select foreign currencies and Canadian dollars with variable exchange rates.
 */
class ExchangeConverter {
    private static double CADtoUSD = 0.74; // Canadian Dollar to United States dollar
//    private static double CADtoEUR; // Canadian Dollar to Euro
//    private static double CADtoGBP; // Canadian Dollar to Great Britain Pound
//    private static double CADtoAUD; // Canadian Dollar to Australian Dollar
//    private static double CADtoJPY; // Canadian Dollar to Japanese Yen
//    private static double CADtoCNY; // Canadian Dollar to Chinese Yuan
//    private static double CADtoINR; // Canadian Dollar to Indian Rupee
//    private static double CADtoBRL; // Canadian Dollar to Brazilian Real
//    private static double CADtoMXN; // Canadian Dollar to Mexican Peso
    private static double USDtoCAD = 1 / ExchangeConverter.CADtoUSD; // United States dollar to Canadian Dollar
//    private static double EURtoCAD; // Euro to Canadian Dollar
//    private static double GBPtoCAD; // Great Britain Pound to Canadian Dollar
//    private static double AUDtoCAD; // Australian Dollar to Canadian Dollar
//    private static double JPYtoCAD; // Japanese Yen to Canadian Dollar
//    private static double CNYtoCAD; // Chinese Yuan to Canadian Dollar
//    private static double INRtoCAD; // Indian Rupee to Canadian Dollar
//    private static double BRLtoCAD; // Brazilian Real to Canadian Dollar
//    private static double MXNtoCAD; // Mexican Peso to Canadian Dollar

    static double ExchangeRate(String convertFrom, String convertTo) {
        double exchangeRate = 1.0;
        double randomMultiplier = randomNumberGenerator();

        if (convertFrom.equals("CAD") && convertTo.equals("USD")) {
            exchangeRate = ExchangeConverter.CADtoUSD;
            ExchangeConverter.CADtoUSD *= randomMultiplier;
        }
        else if (convertFrom.equals("USD") && convertTo.equals("CAD")) {
            exchangeRate = ExchangeConverter.USDtoCAD;
            ExchangeConverter.USDtoCAD *= randomMultiplier;
        }
        return exchangeRate;
    }

    private static double randomNumberGenerator() {
        Random randomGenerator = new Random();
        return (randomGenerator.nextDouble() / 10) + 0.95; // Random number between 0.95 and 1.05
    }
}
