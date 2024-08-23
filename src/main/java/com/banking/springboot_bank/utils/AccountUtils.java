package com.banking.springboot_bank.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been succesfully created!";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided account is not found!";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account found!!";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Amount Credited!";
    public static final String INSUFFICIENT_FUNDS_CODE = "006";
    public static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient balance in account";
    public static final String DEBIT_SUCCESS_CODE = "007";
    public static final String DEBIT_SUCCESS_MESSAGE = "Account has been debited";

    /**
     * 2023 + randomSixDigits, helps us build account number
     */
    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generate a random number between min and max
        int randNumber = (int) Math.floor((Math.random() * (max - min + 1) + min));

        //convert the current and randomNumber to strings, then concatenate
        String year = String.valueOf(currentYear);

        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }
}
