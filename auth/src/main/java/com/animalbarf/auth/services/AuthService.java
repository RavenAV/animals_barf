package com.animalbarf.auth.services;

import com.animalbarf.apicontracts.user.CreateUserDto;
import com.animalbarf.apicontracts.user.UserDto;
import com.animalbarf.auth.pojo.RefreshTokenRequest;
import com.animalbarf.auth.revokeTokens.RevokeUtils;
import com.animalbarf.exceptions.InvalidTokenException;
import com.animalbarf.jwt.TokenProvider;
import com.animalbarf.auth.pojo.LoginRequest;
import com.animalbarf.auth.pojo.LoginResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;
    private final UserService userService;
    private final RevokeUtils revokeUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       @Qualifier("accessTokenProvider") TokenProvider accessTokenProvider,
                       @Qualifier("refreshTokenProvider") TokenProvider refreshTokenProvider,
                       UserService userService,
                       RevokeUtils revokeUtils) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userService = userService;
        this.revokeUtils = revokeUtils;
    }

    /**
     * Вход в аккаунт
     *
     * @param authRequest Запрос
     * @return Пара токенов
     */
    public LoginResponse signIn(@NonNull LoginRequest authRequest) {
        String login = authRequest.getLogin();
        String password = authRequest.getPassword();

        // Аутентификация
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return generateTokens(authentication.getName(), roles);
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
    public LoginResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Валидация токена на подпись и срок || отозван ли он
        if (!refreshTokenProvider.validateToken(refreshToken) || revokeUtils.checkRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        String login = refreshTokenProvider.extractClaims(refreshToken).getSubject();
        Object raw = refreshTokenProvider.extractClaims(refreshToken).get("roles");
        List<String> rolesList = raw instanceof List<?> list
                ? list.stream().map(String::valueOf).toList()
                : List.of();

        // Отзываем устаревший токен
        revokeUtils.revokeToken(refreshToken);
        return generateTokens(login, new HashSet<>(rolesList));
    }

    /**
     * Отзываем токен при выходе
     *
     * @param request Рефреш-токен
     */
    public void logout(RefreshTokenRequest request) {
        revokeUtils.revokeToken(request.getRefreshToken());
    }

    /**
     * Запуск генерации токенов для конкретного пользователя
     *
     * @param login Уникальный идентификатор пользователя (email)
     * @return Пара новых токенов
     */
    private LoginResponse generateTokens(String login, Set<String> roles) {
        // Генерация токенов
        String access = accessTokenProvider.generateToken(login, roles);
        String refresh = refreshTokenProvider.generateToken(login, roles);

        // Сохранение токена в базу
        revokeUtils.saveToken(refresh, login);
        return new LoginResponse(access, refresh);
    }
}