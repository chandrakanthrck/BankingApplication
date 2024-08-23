package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

    @Schema(description = "The name associated with the account.")
    private String accountName;

    @Schema(description = "The current balance of the account.")
    private BigDecimal accountBalance;

    @Schema(description = "The unique account number.")
    private String accountNumber;
}
