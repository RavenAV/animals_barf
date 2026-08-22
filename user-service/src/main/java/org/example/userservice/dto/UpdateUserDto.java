package org.example.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    @Size(max = 100)
    String firstName;

    @Size(max = 100)
    String lastName;

    @Size(max = 100)
    String middleName;

    @Email
    @Size(max = 255)
    String email;

    Boolean enabled;
}
