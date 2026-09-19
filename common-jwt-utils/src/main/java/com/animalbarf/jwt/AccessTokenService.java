package com.animalbarf.jwt;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * Утилита для работы с access-токеном
 */
public class AccessTokenService extends AbstractTokenService {

    public AccessTokenService(
            String secret,
            long expiration
    ) {
        super(secret, expiration);
    }

    @Override
    protected String getTokenName() {
        return "access";
    }

    /**
     * Генерирует access-токен для текущего пользователя с именем пользователя
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
                .claim("username", login)
                .compact();
    }
}
