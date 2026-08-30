package com.animalbarf.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@Qualifier("refreshTokenProvider")
public class RefreshTokenProvider implements TokenProvider {
    private final SecretKey secretKey;
    private final long tokenExpiration;

    public RefreshTokenProvider(
            @Value("${jwt.secret.refresh}") String secret,
            @Value("${jwt.expiration.refresh:604800000}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenExpiration = expiration;
    }

    /**
     * Генерирует refresh-токен для текущего пользователя
     *
     * @param login Логин пользователя
     * @return Токен
     */
    @Override
    public String generateToken(String login) {
        return Jwts.builder()
                .subject(login)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Проверка refresh-токена
     *
     * @param token Токен
     * @return Флаг валидности токена
     */
    @Override
    public Boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException exc) {
            log.error("Refresh token expired", exc);
        } catch (UnsupportedJwtException exc) {
            log.error("Unsupported JWT-Refresh", exc);
        } catch (MalformedJwtException exc) {
            log.error("Malformed  JWT-Refresh", exc);
        } catch (Exception exc) {
            log.error("Invalid refresh token", exc);
        }
        return false;
    }

    @Override
    public Claims extractClaims(String token, SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
