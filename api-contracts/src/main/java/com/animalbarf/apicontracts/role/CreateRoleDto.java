package com.animalbarf.apicontracts.role;

import jakarta.validation.constraints.*;

public record CreateRoleDto(
        @Size(max = 50, message = "Название роли не должно превышать 50 символов")
        String name,

        @Size(max = 255, message = "Описание не должно превышать 255 символов")
        String description
) { }