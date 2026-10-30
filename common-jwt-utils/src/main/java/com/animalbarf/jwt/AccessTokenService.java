package com.animalbarf.jwt;

import io.jsonwebtoken.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Утилита для работы с access-токеном
 */
public class AccessTokenService extends AbstractTokenService {

    public AccessTokenService(
            PrivateKey privateKey,
            PublicKey publicKey,
            long expiration
    ) {
        super(privateKey, publicKey, expiration);
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
                .signWith(privateKey, Jwts.SIG.ES256)
                .claim("username", login)
                .compact();
    }
}
