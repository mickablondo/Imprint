package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;
import lombok.RequiredArgsConstructor;

/**
 * {@link ImprintStore} implementation that stores the serialized object in a file,
 * and returns a short UUID as seed.
 */
@RequiredArgsConstructor
public class FileImprintStore implements ImprintStore {
    
    private final String filePath;

    /**
     * {@inheritDoc}
     *
     * @implSpec Stores the binary data in a file and returns a unique key.
     */
    @Override
    public String save(byte[] data) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Retrieves the binary data from a file using the provided key.
     */
    @Override
    public byte[] load(String key) {
        return new byte[0];
    }
}
