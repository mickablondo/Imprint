package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;

public class JdbcImprintStore implements ImprintStore {
    @Override
    public String save(byte[] data) {
        return "";
    }

    @Override
    public byte[] load(String key) {
        return new byte[0];
    }
}
