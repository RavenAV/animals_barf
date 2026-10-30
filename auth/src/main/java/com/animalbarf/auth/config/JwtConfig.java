package com.animalbarf.auth.config;

import com.animalbarf.auth.tokenSecurity.KeyLoader;
import com.animalbarf.jwt.*;
import com.animalbarf.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class JwtConfig {

    @Bean("accessTokenProvider")
    public TokenProvider accessTokenProvider(@Value("${jwt.secret.access.private}") Resource privateKeyResource,
                                             @Value("${jwt.secret.access.public}") Resource publicKeyResource,
                                             @Value("${jwt.expiration.access:3600000}") long expiration,
                                             KeyLoader keyLoader) throws Exception {
        PrivateKey privateKey = keyLoader.loadPrivateKey(privateKeyResource);
        PublicKey publicKey = keyLoader.loadPublicKey(publicKeyResource);
        return new AccessTokenService(privateKey, publicKey, expiration);
    }

    @Bean("refreshTokenProvider")
    public TokenProvider refreshTokenProvider(@Value("${jwt.secret.refresh.private}") Resource privateKeyResource,
                                              @Value("${jwt.secret.refresh.public}") Resource publicKeyResource,
                                              @Value("${jwt.expiration.refresh:86400000}") long expiration,
                                              KeyLoader keyLoader) throws Exception {
        PrivateKey privateKey = keyLoader.loadPrivateKey(privateKeyResource);
        PublicKey publicKey = keyLoader.loadPublicKey(publicKeyResource);
        return new RefreshTokenService(privateKey, publicKey, expiration);
    }
}