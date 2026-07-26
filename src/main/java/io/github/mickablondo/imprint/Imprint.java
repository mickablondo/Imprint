package io.github.mickablondo.imprint;

import java.util.List;

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

    /**
     * Encodes a list of objects into a list of seed strings.
     *
     * @param objects the list of objects to encode
     * @return a list of encoded seeds
     * @param <T> the type of objects in the list
     */
    default <T> List<String> encodeAll(List<T> objects) {
        return objects.stream()
                .map(this::encode)
                .toList();
    }

    /**
     * Decodes a list of seeds back to their original Java objects.
     *
     * @param seeds the list of seeds to decode
     * @param type  the class type of the objects to decode into
     * @return a list of decoded objects
     * @param <T> the type of objects to decode
     */
    default <T> List<T> decodeAll(List<String> seeds, Class<T> type) {
        return seeds.stream()
                .map(seed -> decode(seed, type))
                .toList();
    }
}
