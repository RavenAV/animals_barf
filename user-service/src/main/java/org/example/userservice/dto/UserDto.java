package org.example.userservice.dto;

public record UserDto (
    Long id,
    String firstName,
    String lastName,
    String middleName,
    String email,
    Boolean enabled
) {

}
