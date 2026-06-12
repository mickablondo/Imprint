package dev.mikablondo.imprint.impl;

import dev.mikablondo.imprint.Imprint;
import dev.mikablondo.imprint.exception.ImprintException;
import dev.mikablondo.imprint.utils.CompressionUtils;
import dev.mikablondo.imprint.utils.SerializationUtils;

/**
 * {@link Imprint} implementation that encodes the serialized object into a self-contained,
 * portable string using compression and Base64 encoding.
 *
 * <p>Encoding process: serialization -> compression -> Base64 -> seed</p>
 * <p>Decoding process: seed -> Base64 -> decompression -> deserialization -> Object</p>
 */
public class ImprintSimple implements Imprint {
    /**
     * {@inheritDoc}
     *
     * @implSpec Serializes, compresses and Base64-encodes the object into a self-contained seed.
     */
    @Override
    public String encode(Object o) {
        if(o == null)
            return "";

        String seed;

        // serialize
        try {
            seed = SerializationUtils.toJson(o);
        } catch (Exception e) {
            throw new ImprintException("Failed to serialize object: " + e.getMessage());
        }

        // compress
        try {
            seed = CompressionUtils.compress(seed);
        } catch (Exception e) {
            throw new ImprintException("Failed to compress object: " + e.getMessage());
        }

        // Base64 encode
        // TODO: implement Base64 encoding

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Base64-decodes, decompresses and deserializes the seed back to its original object.
     */
    @Override
    public Object decode(String s) {
        return null;
    }
}
