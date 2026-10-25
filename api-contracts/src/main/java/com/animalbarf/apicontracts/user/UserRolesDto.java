package com.animalbarf.apicontracts.user;

import java.util.List;
import com.animalbarf.apicontracts.role.*;

public record UserRolesDto(
        Long userId,
        List<RoleDto> roles
) {
}