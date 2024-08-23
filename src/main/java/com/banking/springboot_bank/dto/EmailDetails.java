package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {

    @Schema(description = "The email address of the recipient.")
    private String recipient;

    @Schema(description = "The body of the email message.")
    private String messageBody;

    @Schema(description = "The subject of the email.")
    private String subject;

    @Schema(description = "Path to an attachment to include in the email, if any.")
    private String attachment;
}
