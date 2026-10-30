package com.animalbarf.security.storage;

import java.io.IOException;
import java.security.KeyPair;

public interface KeyPairWriter {
    void save(KeyPair keyPair, String prefix) throws IOException;
}
