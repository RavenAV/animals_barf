package com.animalbarf.auth.services;

import com.animalbarf.apicontracts.user.CreateUserDto;
import com.animalbarf.apicontracts.user.UserAuthDto;
import com.animalbarf.apicontracts.user.UserDto;
import com.animalbarf.auth.pojo.RefreshJwtRequest;
import com.animalbarf.exceptions.InvalidTokenException;
import com.animalbarf.jwt.TokenProvider;
import com.animalbarf.auth.pojo.JwtRequest;
import com.animalbarf.auth.pojo.JwtResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager,
                       @Qualifier("accessTokenProvider") TokenProvider accessTokenProvider,
                       @Qualifier("refreshTokenProvider") TokenProvider refreshTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userService = userService;
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


        // TODO тут сравнение кредов пользователя


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
    public UserDto signUp(@NonNull CreateUserDto request) {
        return userService.create(request);
    }

    /**
     * Обновление refresh-токена
     *
     * @param request Refresh-токен
     * @return Пара токенов с обновленным refresh-токеном
     */
    public JwtResponse refreshToken(@RequestBody @Valid RefreshJwtRequest request) {
        String refreshToken = request.getRefreshToken();
        // Валидация токена
        if (!refreshTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        String login = refreshTokenProvider.extractClaims(refreshToken).getSubject();
        UserAuthDto user = userService.getForAuthentication(login);

        // Генерация новой пары токенов
        String newAccess  = accessTokenProvider.generateToken(user.email());
        String newRefresh = refreshTokenProvider.generateToken(user.email());

        return new JwtResponse(newAccess, newRefresh);
    }
}