package com.animalbarf.auth.controllers;

import com.animalbarf.auth.pojo.JwtRequest;
import com.animalbarf.auth.pojo.JwtResponse;
import com.animalbarf.auth.pojo.RefreshJwtRequest;
import com.animalbarf.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
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
        JwtResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("signup")
    public ResponseEntity<JwtResponse> signUp(@RequestBody JwtRequest request) {
        JwtResponse token = authService.signUp(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
