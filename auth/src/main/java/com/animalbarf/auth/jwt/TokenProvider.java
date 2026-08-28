package com.animalbarf.auth.jwt;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface TokenProvider {
    String generateToken(String login);
    Boolean validateToken(String token);
    Claims extractClaims(String token, SecretKey secret);
}
