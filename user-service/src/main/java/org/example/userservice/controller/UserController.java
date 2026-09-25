package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.CreateUserDto;
import org.example.userservice.dto.UpdateUserDto;
import org.example.userservice.dto.UserAuthDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}
