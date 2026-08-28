package com.animalbarf.auth.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {
    private String refreshToken;
}
