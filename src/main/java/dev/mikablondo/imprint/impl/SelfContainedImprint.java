package dev.mikablondo.imprint.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.mikablondo.imprint.Imprint;
import dev.mikablondo.imprint.core.exception.ImprintError;
import dev.mikablondo.imprint.core.exception.ImprintException;
import dev.mikablondo.imprint.core.utils.Base64Utils;
import dev.mikablondo.imprint.core.utils.CompressionUtils;
import dev.mikablondo.imprint.core.utils.SerializationUtils;

import java.io.IOException;

/**
 * {@link Imprint} implementation that encodes the serialized object into a self-contained,
 * portable string using compression and Base64 encoding.
 *
 * <p>Encoding process: serialization -> compression -> Base64 -> seed</p>
 * <p>Decoding process: seed -> Base64 -> decompression -> deserialization -> Object</p>
 */
public class SelfContainedImprint implements Imprint {
    /**
     * {@inheritDoc}
     *
     * @implSpec Serializes, compresses and Base64-encodes the object into a self-contained seed.
     */
    @Override
    public String encode(Object o) {
        if (o == null) {
            throw new ImprintException(ImprintError.NULL_OBJECT);
        }

        try {
            final byte[] json = SerializationUtils.toJson(o);
            final byte[] compressed = CompressionUtils.compress(json);
            return Base64Utils.encode(compressed);
        } catch (JsonProcessingException e) {
            throw new ImprintException(ImprintError.SERIALIZATION_FAILED, e);
        } catch (IOException e) {
            throw new ImprintException(ImprintError.COMPRESSION_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Base64-decodes, decompresses and deserializes the seed back to its original object.
     */
    @Override
    public <T> T decode(String encoded, Class<T> type) {
        byte[] decoded = Base64Utils.decode(encoded);

        byte[] decompressed;
        try {
            decompressed = CompressionUtils.decompress(decoded);
        } catch (IOException e) {
            throw new ImprintException(ImprintError.DECOMPRESSION_FAILED, e);
        }

        try {
            return SerializationUtils.fromJson(decompressed, type);
        } catch (IOException e) {
            throw new ImprintException(ImprintError.DESERIALIZATION_FAILED, e);
        }
    }
}
