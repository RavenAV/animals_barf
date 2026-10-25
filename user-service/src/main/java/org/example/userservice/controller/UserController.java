package org.example.userservice.controller;

import com.animalbarf.apicontracts.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.service.RoleService;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    /**
     * Создание пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(
            @Valid
            @RequestBody CreateUserDto request
    ) {
        return userService.create(request);
    }

    /**
     * Получение пользователя.
     */
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * Получение всех пользователей.
     */
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    /**
     * Обновление пользователя.
     */
    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable Long id,
            @Valid
            @RequestBody UpdateUserDto request
    ) {
        return userService.update(id, request);
    }

    /**
     * Удаление пользователя.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    /**
     * INTERNAL API
     * Этот endpoint нужен Auth Service.
     */
    @GetMapping("/internal/auth")
    public UserAuthDto getForAuthentication(@RequestParam String email) {
        return userService.getForAuthentication(email);
    }

    // Получить роли пользователя
    @GetMapping("/{userId}/roles")
    public UserRolesDto getUserRoles(@PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    // Назначить роль пользователю
    @PutMapping("/{userId}/roles/{roleId}")
    public UserRolesDto assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        return roleService.assignRole(userId, roleId);
    }

    // Снять роль с пользователя
    @DeleteMapping("/{userId}/roles/{roleId}")
    public UserRolesDto removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        return roleService.removeRole(userId, roleId);
    }
}
