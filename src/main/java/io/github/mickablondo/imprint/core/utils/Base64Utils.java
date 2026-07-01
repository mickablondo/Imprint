package io.github.mickablondo.imprint.core.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

/**
 * Base64 utility class for encoding and decoding byte arrays to and from Base64 strings.
 */
@UtilityClass
public class Base64Utils {
    /**
     * Encodes a byte array into a Base64 string.
     *
     * @param data data to encode
     * @return data encoded as a Base64 string
     */
    public static String encode(byte[]  data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Decodes a Base64 string back into a byte array.
     *
     * @param base64 base64 string to decode
     * @return base64 string decoded back into a byte array
     */
    public static byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
