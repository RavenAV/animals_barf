package com.animalbarf.jwt;

/**
 * Утилита для работы с refresh-токеном
 */
public class RefreshTokenService extends AbstractTokenService {

    public RefreshTokenService(
            String secret,
            long expiration
    ) {
        super(secret, expiration);
    }

    @Override
    protected String getTokenName() {
        return "refresh";
    }
}
