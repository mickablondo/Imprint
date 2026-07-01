package io.github.mickablondo.imprint.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.mickablondo.imprint.Imprint;
import io.github.mickablondo.imprint.ImprintStore;
import io.github.mickablondo.imprint.core.exception.ImprintError;
import io.github.mickablondo.imprint.core.exception.ImprintException;
import io.github.mickablondo.imprint.core.utils.SerializationUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

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
        byte[] data;
        try {
            data = SerializationUtils.toJson(o);
        } catch (JsonProcessingException e) {
            throw new ImprintException(ImprintError.SERIALIZATION_FAILED, e);
        }
        return store.save(data);
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Uses the UUID seed to retrieve and deserialize the object from the store.
     */
    @Override
    public <T> T decode(String encoded, Class<T> type) {
        byte[] data = store.load(encoded);
        try {
            return SerializationUtils.fromJson(data, type);
        } catch (IOException e) {
            throw new ImprintException(ImprintError.DESERIALIZATION_FAILED, e);
        }
    }
}
