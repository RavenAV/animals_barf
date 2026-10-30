package com.animalbarf.keygen;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;

public class KeyGeneratorUtil {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair pair = gen.generateKeyPair();

        // Приватный ключ в формате PKCS#8
        Files.write(Paths.get("ec_private.key"), pair.getPrivate().getEncoded());

        // Публичный ключ в формате X.509
        Files.write(Paths.get("ec_public.key"), pair.getPublic().getEncoded());

        System.out.println("Ключи сгенерированы и сохранены.");
    }
}
