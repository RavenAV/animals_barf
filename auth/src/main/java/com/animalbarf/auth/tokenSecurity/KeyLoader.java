package com.animalbarf.auth.tokenSecurity;

import org.springframework.core.io.Resource;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyLoader {
    PrivateKey loadPrivateKey(Resource resource) throws Exception;

    PublicKey loadPublicKey(Resource resource) throws Exception;
}
