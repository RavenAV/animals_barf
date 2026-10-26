package com.animalbarf.apicontracts.user;

import java.util.List;
import com.animalbarf.apicontracts.role.*;

public record UserAuthDto(
        Long id,
        String email,
        String passwordHash,
        Boolean enabled,
        List<RoleDto> roles
) {
}
