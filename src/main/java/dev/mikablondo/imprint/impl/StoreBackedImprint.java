package dev.mikablondo.imprint.impl;

import dev.mikablondo.imprint.Imprint;
import dev.mikablondo.imprint.ImprintStore;
import lombok.RequiredArgsConstructor;

/**
 * {@link Imprint} implementation that stores the serialized object in an external store,
 * and returns a short UUID as seed.
 *
 * <p>Encoding process: serialization -> store -> UUID</p>
 * <p>Decoding process: UUID -> store lookup -> deserialization -> Object</p>
 */
@RequiredArgsConstructor
public class StoreBackedImprint implements Imprint {

    private final ImprintStore store;

    /**
     * {@inheritDoc}
     *
     * @implSpec Serializes the object, stores it, and returns a UUID as seed.
     */
    @Override
    public String encode(Object o) {
        /*byte[] data = serialize(o);
        return store.save(data);*/
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Uses the UUID seed to retrieve and deserialize the object from the store.
     */
    @Override
    public <T> T decode(String encoded, Class<T> type) {
        /*byte[] data = store.load(key);
        return deserialize(data, type);*/
        return null;
    }
}
