package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDebitRequest {

    @Schema(description = "The unique account number where the credit or debit operation will be performed.")
    private String accountNumber;

    @Schema(description = "The amount to be credited or debited from the account, represented as a decimal value.")
    private BigDecimal amount;
}