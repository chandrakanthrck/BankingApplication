package com.banking.springboot_bank.controller;

import com.banking.springboot_bank.dto.*;
import com.banking.springboot_bank.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs", description = "APIs for managing user accounts including creation, balance enquiry, and transactions.")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creates a new user account and assigns a unique account ID."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User account successfully created."
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){

        return userService.login(loginDto);
    }

    @GetMapping("balanceEnquiry")
    @Operation(
            summary = "Balance Enquiry",
            description = "Checks the balance of a user’s account using the provided account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Balance enquiry successful."
    )
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    @GetMapping("nameEnquiry")
    @Operation(
            summary = "Name Enquiry",
            description = "Retrieves the account holder’s name associated with a given account number."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Name enquiry successful."
    )
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    @PostMapping("credit")
    @Operation(
            summary = "Credit Account",
            description = "Credits a specified amount to the user’s account."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Account successfully credited."
    )
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }

    @PostMapping("debit")
    @Operation(
            summary = "Debit Account",
            description = "Debits a specified amount from the user’s account."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Account successfully debited."
    )
    public BankResponse debitedAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @PostMapping("transfer")
    @Operation(
            summary = "Transfer Funds",
            description = "Transfers funds from one user account to another."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Funds successfully transferred."
    )
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
}