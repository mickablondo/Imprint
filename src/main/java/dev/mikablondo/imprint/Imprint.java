package dev.mikablondo.imprint;

/**
 * Utility for encoding any Java object into a compact, portable string,
 * and decoding it back to its original form.
 */
public class Imprint {

    /**
     * Encodes a Java object into a compact, portable string (seed) using the following process:
     * serialization -> compression -> Base64.
     *
     * @param o the object to encode
     * @return the encoded seed as a Base64 string
     */
    public static String encode(Object o) {
        // TODO
        return null;
    }

    /**
     * Decodes a seed back to its original Java object using the following process:
     * Base64 -> decompression -> deserialization.
     *
     * @param s the seed to decode
     * @return the decoded object
     */
    public static Object decode(String s) {
        // TODO
        return null;
    }
}
