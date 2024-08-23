package com.banking.springboot_bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing the user creation request with necessary user details.")
public class UserRequest {

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's other name(s)", example = "Middle Name")
    private String otherName;

    @Schema(description = "User's gender", example = "Male")
    private String gender;

    @Schema(description = "User's residential address", example = "123 Main St, Springfield")
    private String address;

    @Schema(description = "User's state of origin", example = "California")
    private String stateOfOrigin;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's primary phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "User's alternative phone number", example = "+0987654321")
    private String alternativePhoneNumber;
}