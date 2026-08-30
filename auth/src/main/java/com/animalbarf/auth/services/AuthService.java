package com.animalbarf.auth.services;

import com.animalbarf.jwt.TokenProvider;
import com.animalbarf.auth.pojo.JwtRequest;
import com.animalbarf.auth.pojo.JwtResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
        );


        // TODO ожидает реализации взаимодействия между МС - тут сравнение кредов пользователя


        String accessToken = accessTokenProvider.generateToken(authRequest.getLogin());
        String refreshToken = refreshTokenProvider.generateToken(authRequest.getLogin());
        return new JwtResponse(accessToken, refreshToken);
    }

    /**
     * Регистрация
     *
     * @param request Запрос
     * @return Результат регистрации: успешно или нет
     */
    public JwtResponse signUp(@NonNull JwtRequest request) {
        // TODO ожидает реализации взаимодействия между МС - тут не должен возвращать токены
        return new JwtResponse(null, null);
    }

    /**
     * Получение токенов
     *
     * @param request Запрос
     * @return Пара токенов
     */
    public JwtResponse getTokens(@NonNull String request) {
        // TODO ожидает реализации взаимодействия между МС
        return new JwtResponse(null, null);
    }

    /**
     * Обновление refresh-токена
     *
     * @param request Запрос
     * @return Пара токенов с обновленным refresh-токеном
     */
    public JwtResponse refreshToken(@NonNull String request) {
        // TODO ожидает реализации взаимодействия между МС
        return new JwtResponse(null, null);
    }
}