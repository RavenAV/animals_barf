package com.animalbarf.jwt;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Утилита для работы с refresh-токеном
 */
public class RefreshTokenService extends AbstractTokenService {

    public RefreshTokenService(
            PrivateKey privateKey,
            PublicKey publicKey,
            long expiration
    ) {
        super(privateKey, publicKey, expiration);
    }

    @Override
    protected String getTokenName() {
        return "refresh";
    }
}
