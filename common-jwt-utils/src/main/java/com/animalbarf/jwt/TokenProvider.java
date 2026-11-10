package com.animalbarf.jwt;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.Set;

public interface TokenProvider {
    String generateToken(String login, Set<String> roles);
    Boolean validateToken(String token);
    Claims extractClaims(String token);
}
