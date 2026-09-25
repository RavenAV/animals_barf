package org.example.userservice.dto;

import jakarta.validation.constraints.*;

public record CreateUserDto(
        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100)
        String lastName,

        @Size(max = 100)
        String middleName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Password is required")
        @Size(
                min = 8,
                max = 100,
                message = "Password must contain 8-100 characters"
        )
        String password
) {
}