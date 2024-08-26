package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Schema(description = "The email address of the user", example = "user@example.com")
    private String email;
    @Schema(description = "The password of the user", example = "P@ssw0rd!")
    private String password;
}
