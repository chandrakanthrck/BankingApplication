package com.banking.springboot_bank.service.impl;

import com.banking.springboot_bank.dto.BankResponse;
import com.banking.springboot_bank.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
