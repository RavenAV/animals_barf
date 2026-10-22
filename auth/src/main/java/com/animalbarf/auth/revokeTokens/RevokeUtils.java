package com.animalbarf.auth.revokeTokens;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RevokeUtils {
    public final Map<String, RevokeTokenEntity> tokens = new ConcurrentHashMap<>();

    /**
     * Сохранение нового токена
     *
     * @param token Токен
     */
    public void saveToken(String token, String login) {
        RevokeTokenEntity entity = new RevokeTokenEntity();
        entity.setToken(token);
        entity.setLogin(login);
        entity.setRevokedAt(null);
        tokens.put(token, entity);
    }

    /**
     * Отозван ли токен
     *
     * @param token Токен
     * @return Флаг, отозван ли токен
     */
    public boolean checkRefreshToken(String token) {
        RevokeTokenEntity entity = tokens.get(token);

        // Токена нет в памяти
        if (entity == null) return false;

        // Токен отозван (logout)
        return entity.getRevokedAt() != null;
    }

    /**
     * Отзыв токена
     *
     * @param token Токен
     */
    public void revokeToken(String token) {
        RevokeTokenEntity entity = tokens.get(token);
        if (entity != null) {
            entity.setRevokedAt(LocalDateTime.now());
        }
    }
}
