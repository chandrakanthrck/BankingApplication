package com.banking.springboot_bank.service;

import com.banking.springboot_bank.config.JwtTokenProvider;
import com.banking.springboot_bank.dto.*;
import com.banking.springboot_bank.entity.Role;
import com.banking.springboot_bank.entity.User;
import com.banking.springboot_bank.repository.UserRepository;
import com.banking.springboot_bank.service.impl.EmailService;
import com.banking.springboot_bank.service.impl.TransactionService;
import com.banking.springboot_bank.service.impl.UserService;
import com.banking.springboot_bank.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    //recording transaction
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    //for security
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        // Check if user already has an account
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Create a new user account
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                //encrypting the password received from request
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();

        User savedUser = userRepository.save(newUser);
        //after creating the user we want to send an email to the user
        EmailDetails emailDetails = EmailDetails.builder().
                recipient(savedUser.getEmail()).
                subject("ACCOUNT CREATION").
                messageBody("Congratulations! Your account has been successfully created!\n" +
                        "Your Account Details:" + "Account Name: " + " " +
                        savedUser.getFirstName() + " " + savedUser.getLastName()
                        + " " + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber()).
                build();
        //call to send the email
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountinfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " +
                                savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }

    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        EmailDetails loginAlert = EmailDetails.builder().
                subject("You logged in!").
        recipient(loginDto.getEmail()).
                messageBody("You logged into your account. If you did not initiate this request, please contact the bank").
                build();
        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder().
        responseCode("Login Success").
        responseMessage(jwtTokenProvider.generateToken(authentication)).build();
    }

    //balance enquiry, name enquiry, credit, debit, transfer
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // check if the provided account number exists in the database
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder().
                    responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).
                    accountinfo(null).build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder().
        responseCode(AccountUtils.ACCOUNT_FOUND_CODE).responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS).
        accountinfo(AccountInfo.builder().accountBalance(foundUser.getAccountBalance())
                .accountNumber(request.getAccountNumber()).
                accountName(foundUser.getFirstName()+ " " + foundUser.getLastName()
                + " " + foundUser.getOtherName()).build()).build() ;
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists in the database
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " "  + foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        //checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder().
                    responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).
                    accountinfo(null).build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        //crediting money to the user
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        //save transaction
        TransactionDto transactionDto = TransactionDto.builder().
        accountNumber(userToCredit.getAccountNumber()).transactionType("CREDIT").amount(request.getAmount()).build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder().
        responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS).
                responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE).
                accountinfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()
                + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName()).
                        accountBalance(userToCredit.getAccountBalance()).
                        accountNumber(request.getAccountNumber()).build()).build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        //checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder().
                    responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).
                    accountinfo(null).build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        if (request.getAmount() == null) {
            // Handle the null case, maybe return an error response or throw an exception
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage("The amount cannot be null.")
                    .build();
        }

        if (request.getAmount() == null) {
            // Handle the null case, maybe return an error response or throw an exception
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage("The amount cannot be null.")
                    .build();
        }
        //checking if the amount exceeds the balance of the user
        if (userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0) {
            // Balance is less than the amount to be debited
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .responseMessage("Insufficient balance")
                    .accountinfo(null)
                    .build();
        }
        else {
            // Balance is sufficient, proceed with debit
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);  // Save the updated balance
            //Record transaction
            TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToDebit.getAccountNumber()).
                    transactionType("DEBIT").amount(request.getAmount()).build();
            transactionService.saveTransaction(transactionDto);
            return BankResponse.builder()
                    .responseCode(AccountUtils.DEBIT_SUCCESS_CODE)
                    .responseMessage(AccountUtils.DEBIT_SUCCESS_MESSAGE)
                    .accountinfo(AccountInfo.builder()
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .build())
                    .build();
        }

    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        //get the account to debit (Check if the account exists)
        //check if the amount I am debiting is not more than the current account balance
        //debit the account
        //get the account to credit
        //credit the account
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return BankResponse.builder().
                    responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE).
                    responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).
                    accountinfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount() == null) {
            // Handle the null case, maybe return an error response or throw an exception
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage("The amount cannot be null.")
                    .build();
        }
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .responseMessage("Insufficient balance")
                    .accountinfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + " " + sourceAccountUser.getOtherName();

        userRepository.save(sourceAccountUser);
        EmailDetails debitAlerts = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current balance is " +
                        sourceAccountUser.getAccountBalance()).build();
        emailService.sendEmailAlert(debitAlerts);
        System.out.println("Sending email to: " + debitAlerts.getRecipient());

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        String recipientUsername = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName() + " " + destinationAccountUser.getOtherName();
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlerts = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() + " has been sent to your account from " + sourceUserName + " Your current balance is " +
                        destinationAccountUser.getAccountBalance() + ". This message is intended for " + recipientUsername + ".")
                        .build();
        emailService.sendEmailAlert(creditAlerts);

        TransactionDto transactionDto = TransactionDto.builder().
                accountNumber(destinationAccountUser.getAccountNumber()).transactionType("CREDIT").amount(request.getAmount()).build();

        transactionService.saveTransaction(transactionDto);

        System.out.println("Sending email to: " + creditAlerts.getRecipient());
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountinfo(AccountInfo.builder()
                        .accountBalance(sourceAccountUser.getAccountBalance())
                        .accountNumber(sourceAccountUser.getAccountNumber())
                        .accountName(sourceUserName)
                        .build())
                .build();
    }
}
