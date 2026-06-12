package dev.mikablondo.imprint.impl;

import dev.mikablondo.imprint.Imprint;

/**
 * {@link Imprint} implementation that stores the serialized object in an external store,
 * and returns a short UUID as seed.
 *
 * <p>Encoding process: serialization -> store -> UUID</p>
 * <p>Decoding process: UUID -> store lookup -> deserialization -> Object</p>
 */
public class ImprintStore implements Imprint {
    /**
     * {@inheritDoc}
     *
     * @implSpec Serializes the object, stores it, and returns a UUID as seed.
     */
    @Override
    public String encode(Object o) {
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Uses the UUID seed to retrieve and deserialize the object from the store.
     */
    @Override
    public Object decode(String s) {
        return null;
    }
}
