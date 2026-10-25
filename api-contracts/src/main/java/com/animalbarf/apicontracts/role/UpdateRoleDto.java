package com.animalbarf.apicontracts.role;

import jakarta.validation.constraints.*;

public record UpdateRoleDto(
        @Size(max = 50)
        String name,

        @Size(max = 255)
        String description
) { }