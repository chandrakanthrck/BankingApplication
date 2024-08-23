package com.banking.springboot_bank.service.impl;

import com.banking.springboot_bank.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
