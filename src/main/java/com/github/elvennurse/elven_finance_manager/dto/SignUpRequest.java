package com.github.elvennurse.elven_finance_manager.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SignUpRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 40, message = "Username must be between 4 and 40 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
