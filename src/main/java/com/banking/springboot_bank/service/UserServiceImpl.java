package com.banking.springboot_bank.service;

import com.banking.springboot_bank.dto.AccountInfo;
import com.banking.springboot_bank.dto.BankResponse;
import com.banking.springboot_bank.dto.EmailDetails;
import com.banking.springboot_bank.dto.UserRequest;
import com.banking.springboot_bank.entity.User;
import com.banking.springboot_bank.repository.UserRepository;
import com.banking.springboot_bank.service.impl.EmailService;
import com.banking.springboot_bank.service.impl.UserService;
import com.banking.springboot_bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

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
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
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
}
