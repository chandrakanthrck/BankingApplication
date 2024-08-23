package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {

    @Schema(description = "The code indicating the result of the bank operation. Commonly used for status or error codes.")
    private String responseCode;

    @Schema(description = "A message providing additional details about the operation result, typically used for user-friendly descriptions.")
    private String responseMessage;

    @Schema(description = "Detailed account information associated with the response, including account name, balance, and number.")
    private AccountInfo accountinfo;
}
