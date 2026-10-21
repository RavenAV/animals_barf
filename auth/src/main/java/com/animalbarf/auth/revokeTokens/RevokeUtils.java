package com.animalbarf.auth.revokeTokens;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RevokeUtils {
    public final Map<String, RevokeTokenEntity> tokens = new ConcurrentHashMap<>();

    /**
     * Сохранение нового токена
     *
     * @param token Токен
     */
    public void saveToken(String token, String userId) {
        RevokeTokenEntity entity = new RevokeTokenEntity();
        entity.setToken(token);
        entity.setUserId(userId);
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
        return entity.getRevokedAt() == null;
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
