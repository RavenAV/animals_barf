package com.animalbarf.apicontracts.user;

public record UserAuthDto(
        Long id,
        String email,
        String passwordHash,
        Boolean enabled
) {
}
