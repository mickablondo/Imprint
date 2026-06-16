package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ImprintStore} implementation that stores the serialized object in memory,
 * and returns a short UUID as seed.
 */
public class InMemoryImprintStore implements ImprintStore {

    // one map by instance, so that we can have multiple instances of the store with different data
    private final Map<String, byte[]> storage = new ConcurrentHashMap<>();

    @Override
    public String save(byte[] data) {
        String key;

        // create a unique key by generating a random UUID and taking the first 8 characters
        do {
            key = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8);
        } while (storage.containsKey(key));

        storage.put(key, data);
        return key;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Retrieves the binary data from a database using the provided key.
     */
    @Override
    public byte[] load(String key) {
        return storage.get(key);
    }
}
