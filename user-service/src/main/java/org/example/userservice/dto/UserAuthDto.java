package org.example.userservice.dto;

public record UserAuthDto(
        Long id,
        String email,
        String passwordHash,
        Boolean enabled
) {
}
