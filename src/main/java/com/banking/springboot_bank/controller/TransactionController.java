package com.banking.springboot_bank.controller;

import com.banking.springboot_bank.entity.Transaction;
import com.banking.springboot_bank.entity.User;
import com.banking.springboot_bank.repository.UserRepository;
import com.banking.springboot_bank.service.impl.BankStatement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankstatement")
@AllArgsConstructor
@Tag(name = "Bank Statement APIs", description = "APIs related to generating bank statements.")
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    @Autowired
    private UserRepository userRepository;  // Inject UserRepository

    @Operation(
            summary = "Generate Bank Statement",
            description = "Generates a bank statement in PDF format for a specified account number and date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank statement generated successfully."),
            @ApiResponse(responseCode = "404", description = "Account not found."),
            @ApiResponse(responseCode = "500", description = "Error occurred while generating the statement.")
    })
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

