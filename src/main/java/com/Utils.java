package com;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {

    private final static Random RANDOM = new SecureRandom();
    private final static String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String[] CUSTOMER_NAMES = {"Rishi Kaul", "Khushi Jashnani", "Priyav Mehta"};
    private static final String[] CUSTOMER_CARD_NUMBER = {"9402168345052290", "8829457812904389", "7834902187452330"};
    private static final String[] MERCHANT_NAMES = {"Amazon", "Flipkart", "Snapdeal"};
    private static final String[] MERCHANT_ACCOUNT_ID = {"02489", "17483", "29074"};

    public static String generateTransactionID(int length) {
        return generateRandomString(length);
    }
    private static String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }
    public static String[] generateCustomerDetails() {
        int no = RANDOM.nextInt(CUSTOMER_NAMES.length);
        String[] returnValue = new String[2];
        returnValue[0] = CUSTOMER_CARD_NUMBER[no];
        returnValue[1] = CUSTOMER_NAMES[no];
        return returnValue;
    }

    public static String[] getMerchantDetails() {
        int no = RANDOM.nextInt(MERCHANT_ACCOUNT_ID.length);
        String[] returnValue = new String[2];
        returnValue[0] = MERCHANT_ACCOUNT_ID[no];
        returnValue[1] = MERCHANT_NAMES[no];
        return returnValue;
    }
}