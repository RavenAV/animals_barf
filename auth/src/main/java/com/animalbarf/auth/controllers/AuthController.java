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
        JwtResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<JwtResponse> signUp(@RequestBody JwtRequest request) {
        JwtResponse response = authService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.getRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
