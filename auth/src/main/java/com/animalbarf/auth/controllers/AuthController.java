package com.animalbarf.auth.controllers;

import com.animalbarf.apicontracts.user.CreateUserDto;
import com.animalbarf.apicontracts.user.UserDto;
import com.animalbarf.auth.pojo.LoginRequest;
import com.animalbarf.auth.pojo.LoginResponse;
import com.animalbarf.auth.pojo.RefreshTokenRequest;
import com.animalbarf.auth.services.AuthService;
import com.animalbarf.exceptions.InvalidTokenException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid CreateUserDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> getNewRefreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            LoginResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
