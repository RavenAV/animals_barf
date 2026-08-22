package com.animalbarf.auth.services;

import com.animalbarf.auth.jwt.TokenProvider;
import com.animalbarf.auth.pojo.JwtRequest;
import com.animalbarf.auth.pojo.JwtResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;


    public AuthService(AuthenticationManager authenticationManager,
                       @Qualifier("accessTokenProvider") TokenProvider accessTokenProvider,
                       @Qualifier("refreshTokenProvider") TokenProvider refreshTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    /**
     * Вход в аккаунт
     *
     * @param authRequest Запрос
     * @return Пара токенов
     */
    public JwtResponse signIn(@NonNull JwtRequest authRequest) {
        // TODO ожидает реализации взаимодействия между МС
        return new JwtResponse(null, null);
    }

    /**
     * Регистрация
     *
     * @param request Запрос
     * @return Результат регистрации: успешно или нет
     */
    public JwtResponse signUp(@NonNull JwtRequest request) {
        // TODO ожидает реализации взаимодействия между МС
        return new JwtResponse(null, null);
    }

    /**
     * Получение access-токена
     *
     * @param request Запрос
     * @return Пара токенов
     */
    public JwtResponse getAccessToken(@NonNull String request) {
        // TODO ожидает реализации взаимодействия между МС
        return new JwtResponse(null, null);
    }

    /**
     * Обновление refresh-токена
     *
     * @param request Запрос
     * @return Пара токенов с обновленным refresh-токеном
     */
    public JwtResponse getRefreshToken(@NonNull String request) {
        // TODO ожидает реализации взаимодействия между МС
        return new JwtResponse(null, null);
    }
}