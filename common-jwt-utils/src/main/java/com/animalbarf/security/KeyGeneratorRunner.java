package com.animalbarf.security;

import com.animalbarf.security.generator.KeyGeneratorECDSA;
import com.animalbarf.security.storage.KeyPairFileStorage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;

public class KeyGeneratorRunner {
    public static void main(String[] args) throws Exception {
        // Настраиваем зависимости
        KeyGeneratorECDSA generator = new KeyGeneratorECDSA();
        Path keysDirectory = Paths.get("D:/DevelopJava/animals_barf/common-jwt-utils");
        KeyPairFileStorage storage = new KeyPairFileStorage(keysDirectory);

        // Генерируем и сохраняем Access
        KeyPair accessPair = generator.generateKeyPair();
        storage.save(accessPair, "access");

        // Генерируем и сохраняем Refresh
        KeyPair refreshPair = generator.generateKeyPair();
        storage.save(refreshPair, "refresh");

        System.out.println("Ключи успешно сгенерированы и сохранены в: " + keysDirectory.toAbsolutePath());
    }
}