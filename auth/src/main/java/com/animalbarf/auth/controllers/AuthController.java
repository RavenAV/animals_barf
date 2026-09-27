package com.animalbarf.auth.controllers;

import com.animalbarf.apicontracts.user.CreateUserDto;
import com.animalbarf.apicontracts.user.UserDto;
import com.animalbarf.auth.pojo.JwtRequest;
import com.animalbarf.auth.pojo.JwtResponse;
import com.animalbarf.auth.pojo.RefreshJwtRequest;
import com.animalbarf.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> signIn(@RequestBody JwtRequest request) {
        JwtResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<UserDto> signUp(@RequestBody CreateUserDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request));
    }

    @PostMapping("tokens")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.getTokens(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
