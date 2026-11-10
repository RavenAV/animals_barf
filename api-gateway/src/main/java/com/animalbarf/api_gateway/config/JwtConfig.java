package com.animalbarf.api_gateway.config;

import com.animalbarf.apicontracts.securityKeyLoader.KeyLoaderECDSA;
import com.animalbarf.jwt.*;
import com.animalbarf.jwt.TokenProvider;
import com.animalbarf.apicontracts.securityKeyLoader.KeyLoader;
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
                                             @Value("${jwt.expiration.access:3600000}") long expiration) throws Exception {
        KeyLoader keyLoader = new KeyLoaderECDSA();
        PrivateKey privateKey = keyLoader.loadPrivateKey(privateKeyResource);
        PublicKey publicKey = keyLoader.loadPublicKey(publicKeyResource);
        return new AccessTokenService(privateKey, publicKey, expiration);
    }
}