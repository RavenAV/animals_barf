package com.animalbarf.security.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;

public class KeyPairFileStorage implements KeyPairWriter {
    private final Path directory;

    public KeyPairFileStorage(Path directory) {
        this.directory = directory;
    }

    public void save(KeyPair keyPair, String prefix) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Сохраняем приватный (PKCS#8) и публичный (X.509) ключи
        Path privateKeyPath = directory.resolve(prefix + "_private.key");
        Path publicKeyPath = directory.resolve(prefix + "_public.key");

        Files.write(privateKeyPath, keyPair.getPrivate().getEncoded());
        Files.write(publicKeyPath, keyPair.getPublic().getEncoded());
    }
}