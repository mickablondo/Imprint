package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;

public class FileImprintStore implements ImprintStore {
    @Override
    public String save(byte[] data) {
        return null;
    }

    @Override
    public byte[] load(String key) {
        return new byte[0];
    }
}
