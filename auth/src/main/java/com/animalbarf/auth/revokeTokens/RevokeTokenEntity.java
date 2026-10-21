package com.animalbarf.auth.revokeTokens;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class RevokeTokenEntity {
    @Setter
    @Getter
    private String token;
    @Setter
    @Getter
    private String userId;
    @Setter
    @Getter
    private LocalDateTime revokedAt; // дата отзыва, если отозван
}
