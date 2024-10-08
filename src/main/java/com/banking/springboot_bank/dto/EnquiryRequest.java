package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// For balance and name enquiry
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnquiryRequest {

    @Schema(description = "The account number associated with the user's bank account.")
    private String accountNumber;
}
