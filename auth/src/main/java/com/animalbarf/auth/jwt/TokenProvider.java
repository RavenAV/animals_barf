package com.animalbarf.auth.jwt;

import com.animalbarf.auth.domain.User;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface TokenProvider {
    String generateToken(User user);
    Boolean validateToken(String token);
    Claims extractClaims(String token, SecretKey secret);
}
