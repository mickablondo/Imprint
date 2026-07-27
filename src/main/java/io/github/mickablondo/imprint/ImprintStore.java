package io.github.mickablondo.imprint;

import java.time.Instant;
import java.util.Optional;

/**
 * ImprintStore is an interface that defines the contract for storing and retrieving binary data.
 */
public interface ImprintStore {
    /**
     * Saves the binary data to the store and returns a unique key.
     *
     * @param data the binary data to save
     * @return the unique key for the saved data
     */
    String save(byte[] data);

    /**
     * Loads the binary data from the store using the provided key.
     *
     * @param key the unique key for the data to load
     * @return the binary data
     */
    byte[] load(String key);

    /**
     * Returns the creation date of the data associated with the provided key, if available.
     * @param key the unique key for the data
     * @return the creation date, or an empty Optional if not available
     */
    default Optional<Instant> getCreationDate(String key) {
        return Optional.empty();
    }
}
