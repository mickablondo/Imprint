package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;

/**
 * {@link ImprintStore} implementation that stores the serialized object in a database,
 * and returns a short UUID as seed.
 */
public class JdbcImprintStore implements ImprintStore {
    /**
     * {@inheritDoc}
     *
     * @implSpec Stores the binary data in memory and returns a unique key.
     */
    @Override
    public String save(byte[] data) {
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Retrieves the binary data from memory using the provided key.
     */
    @Override
    public byte[] load(String key) {
        return new byte[0];
    }
}
