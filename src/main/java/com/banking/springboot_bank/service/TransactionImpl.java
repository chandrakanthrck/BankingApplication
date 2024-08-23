package com.banking.springboot_bank.service;

import com.banking.springboot_bank.dto.TransactionDto;
import com.banking.springboot_bank.entity.Transaction;
import com.banking.springboot_bank.repository.TransactionRepository;
import com.banking.springboot_bank.service.impl.TransactionService;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//impl for recording transactions
@Component
public class TransactionImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    //saving transaction dto info into transaction
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder().transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully!!");
    }
}
