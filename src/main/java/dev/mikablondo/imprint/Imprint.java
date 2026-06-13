package dev.mikablondo.imprint;

/**
 * Contract for encoding any Java object into a portable string (seed),
 * and decoding it back to its original form.
 */
public interface Imprint {

    /**
     * Encodes a Java object into a portable seed string.
     *
     * @param o the object to encode
     * @return the encoded seed
     */
    String encode(Object o);

    /**
     * Decodes a seed back to its original Java object.
     *
     * @param encoded the seed to decode
     * @param type    the class type of the object to decode into
     * @return the decoded object
     */
    <T> T decode(String encoded, Class<T> type);
}
