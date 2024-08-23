package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    @Schema(description = "The account number from which the amount will be transferred.")
    private String sourceAccountNumber;

    @Schema(description = "The account number to which the amount will be transferred.")
    private String destinationAccountNumber;

    @Schema(description = "The amount of money to be transferred.")
    private BigDecimal amount;
}