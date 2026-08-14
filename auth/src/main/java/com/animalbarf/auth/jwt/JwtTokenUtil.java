package com.animalbarf.auth.jwt;

import com.animalbarf.auth.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Отвечает за генерацию и валидацию токенов
 */
@Slf4j
@Component
public class JwtTokenUtil {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenUtil(
            @Value("${jwt.secret.access}") String accessSecret,
            @Value("${jwt.secret.refresh}") String refreshSecret,
            @Value("${jwt.expiration.access:3600000}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh:604800000}") long refreshTokenExpiration
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Извлечение данных из токена с заданным ключом
     *
     * @param token  Токен
     * @param secret Ключ по типу токена
     * @return Данные пользователя из токена
     */
    private Claims extractClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Общая валидация токена с заданным ключом
     *
     * @param token  Токен
     * @param secret Ключ по типу токена
     * @return Флаг валидности токена
     */
    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException exc) {
            log.error("Token expired", exc);
        } catch (UnsupportedJwtException exc) {
            log.error("Unsupported JWT", exc);
        } catch (MalformedJwtException exc) {
            log.error("Malformed  JWT", exc);
        } catch (Exception exc) {
            log.error("Invalid token", exc);
        }
        return false;
    }

    /**
     * Проверка access-токена
     *
     * @param token Токен
     * @return Флаг валидности токена
     */
    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, accessSecretKey);
    }

    /**
     * Проверка refresh-токена
     *
     * @param token Токен
     * @return Флаг валидности токена
     */
    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, refreshSecretKey);
    }

    /**
     * Генерирует access-токен для текущего пользователя
     *
     * @param user Пользователь
     * @return Токен
     */
    public String generateAccessToken(@NonNull User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(accessSecretKey)
                .claim("username", user.getLogin())
                .compact();
    }

    /**
     * Генерирует refresh-токен для текущего пользователя
     *
     * @param user Пользователь
     * @return Токен
     */
    public String generateRefreshToken(@NonNull User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(refreshSecretKey)
                .compact();
    }
}