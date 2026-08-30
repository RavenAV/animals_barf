package com.animalbarf.auth.config;

import com.animalbarf.jwt.*;
import com.animalbarf.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean("accessTokenProvider")
    public TokenProvider accessTokenProvider(
            @Value("${jwt.secret.access}") String secret,
            @Value("${jwt.expiration.access:3600000}") long expiration) {
        return new AccessTokenProvider(secret, expiration);
    }

    @Bean("refreshTokenProvider")
    public TokenProvider refreshTokenProvider(
            @Value("${jwt.secret.refresh}") String secret,
            @Value("${jwt.expiration.refresh:86400000}") long expiration) {
        return new RefreshTokenProvider(secret, expiration);
    }
}