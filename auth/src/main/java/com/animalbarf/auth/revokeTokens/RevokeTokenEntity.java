package com.animalbarf.auth.revokeTokens;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RevokeTokenEntity {
    private String token;
    private String login;
    private LocalDateTime revokedAt; // дата отзыва, если отозван
}
