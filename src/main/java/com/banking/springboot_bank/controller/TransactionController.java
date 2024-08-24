package com.banking.springboot_bank.controller;

import com.banking.springboot_bank.entity.Transaction;
import com.banking.springboot_bank.entity.User;
import com.banking.springboot_bank.repository.UserRepository;
import com.banking.springboot_bank.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankstatement")
@AllArgsConstructor
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    @Autowired
    private UserRepository userRepository;  // Inject UserRepository

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws FileNotFoundException {
        // Fetch transactions
        List<Transaction> transactions = bankStatement.generateStatement(accountNumber, startDate, endDate);

        // Fetch user data based on account number
        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName();

        // Generate PDF statement
        bankStatement.designStatement(transactions, startDate, endDate, customerName, user);

        return transactions;
    }
}

