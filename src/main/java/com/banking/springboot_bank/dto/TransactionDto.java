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
public class TransactionDto {
    @Schema(description = "The type of transaction, e.g., 'credit' or 'debit'", example = "credit")
    private String transactionType;
    @Schema(description = "The amount involved in the transaction", example = "1000.00")
    private BigDecimal amount;
    @Schema(description = "The account number associated with the transaction", example = "123456789")
    private String accountNumber;
    @Schema(description = "The current status of the transaction", example = "completed")
    private String status;
}
