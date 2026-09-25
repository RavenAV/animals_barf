package org.example.userservice.dto;

import jakarta.validation.constraints.*;

public record UpdateUserDto (
        Long id,

        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Size(max = 100)
        String middleName,

        @Email
        @Size(max = 255)
        String email,

        Boolean enabled
) {

}
