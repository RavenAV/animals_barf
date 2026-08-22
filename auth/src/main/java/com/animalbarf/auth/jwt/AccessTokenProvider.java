package com.animalbarf.auth.jwt;

import com.animalbarf.auth.domain.User;
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
@Qualifier("accessTokenProvider")
public class AccessTokenProvider implements TokenProvider {
    private final SecretKey secretKey;
    private final long tokenExpiration;


    public AccessTokenProvider(
            @Value("${jwt.secret.access}") String accessSecret,
            @Value("${jwt.expiration.access:3600000}") long accessTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.tokenExpiration = accessTokenExpiration;
    }

    /**
     * Генерирует access-токен для текущего пользователя
     *
     * @param user Пользователь
     * @return Токен
     */
    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(secretKey)
                .claim("username", user.getLogin())
                .compact();
    }

    /**
     * Проверка access-токена
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
            log.error("Access token expired", exc);
        } catch (UnsupportedJwtException exc) {
            log.error("Unsupported JWT-Access", exc);
        } catch (MalformedJwtException exc) {
            log.error("Malformed  JWT-Access", exc);
        } catch (Exception exc) {
            log.error("Invalid access token", exc);
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
