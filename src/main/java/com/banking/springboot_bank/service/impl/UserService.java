package com.banking.springboot_bank.service.impl;

import com.banking.springboot_bank.dto.BankResponse;
import com.banking.springboot_bank.dto.CreditDebitRequest;
import com.banking.springboot_bank.dto.EnquiryRequest;
import com.banking.springboot_bank.dto.UserRequest;

public interface UserService {
    //any functionality you want to add, you will define it here
    BankResponse createAccount(UserRequest userRequest);
    //balance enquiry
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
