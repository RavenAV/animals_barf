package com.animalbarf.auth.services;

import com.animalbarf.auth.pojo.JwtRequest;
import com.animalbarf.auth.pojo.JwtResponse;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        // обращение к мс пользователей (а пока временно хардкодный пароль и логин) - вспомнить как обращаться
        return new JwtResponse(null, null);
    }

    public JwtResponse getAccessToken(@NonNull String request) {
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String request) {
        return new JwtResponse(null, null);
    }

    public JwtResponse signUp(@NonNull JwtRequest request) {
        return new JwtResponse(null, null);
    }
}