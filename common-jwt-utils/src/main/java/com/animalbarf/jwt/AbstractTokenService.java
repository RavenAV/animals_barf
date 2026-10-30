package com.animalbarf.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.util.Date;

@Slf4j
public abstract class AbstractTokenService implements TokenProvider {
    protected final PrivateKey privateKey;
    protected final PublicKey publicKey;
    protected final long tokenExpiration;

    public AbstractTokenService(
            PrivateKey privateKey,
            PublicKey publicKey,
            long expiration
    ) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.tokenExpiration = expiration;
    }

    protected abstract String getTokenName();

    /**
     * Генерирует токен для текущего пользователя
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
                .compact();
    }

    /**
     * Проверка токена
     *
     * @param token Токен
     * @return Флаг валидности токена
     */
    @Override
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException exc) {
            log.error("Срок действия {}-токена истёк", getTokenName(), exc);
        } catch (UnsupportedJwtException exc) {
            log.error("Неподдерживаемый формат {}-токена", getTokenName(), exc);
        } catch (MalformedJwtException exc) {
            log.error("Некорректный {}-токен", getTokenName(), exc);
        } catch (Exception exc) {
            log.error("Не удалось проверить {}-токен", getTokenName(), exc);
        }
        return false;
    }

    /**
     * Получение данных из токена
     * @param token Токен
     * @return Данные из токена
     */
    @Override
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
