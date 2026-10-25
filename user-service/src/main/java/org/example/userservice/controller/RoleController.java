package org.example.userservice.controller;

import com.animalbarf.apicontracts.role.CreateRoleDto;
import com.animalbarf.apicontracts.role.RoleDto;
import com.animalbarf.apicontracts.role.UpdateRoleDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    // Получить все роли
    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Получить роль по ID
    @GetMapping("/{roleId}")
    public RoleDto getRoleById(@PathVariable Long roleId) {
        return roleService.getRoleById(roleId);
    }

    // Создать роль
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDto createRole(@Valid @RequestBody CreateRoleDto request) {
        return roleService.createRole(request);
    }

    // Изменить роль
    @PutMapping("/{roleId}")
    public RoleDto updateRole(
            @PathVariable Long roleId,
            @Valid @RequestBody UpdateRoleDto request
    ) {
        return roleService.updateRole(roleId, request);
    }

    // Удалить роль
    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
    }
}
