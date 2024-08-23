package com.banking.springboot_bank.dto;

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
    private String sourceAccountNumber;
    private String DestinationAccountNumber;
    private BigDecimal amount;
}
