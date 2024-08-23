package com.banking.springboot_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//for credit and debit card
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnquiryRequest {
    private String accountNumber;
}
