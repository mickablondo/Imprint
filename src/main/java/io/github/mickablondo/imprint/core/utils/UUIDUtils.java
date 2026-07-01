package io.github.mickablondo.imprint.core.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * UUIDUtils is a utility class that provides methods for generating unique identifiers (UUIDs).
 * It contains a method to generate a short UUID string without dashes, which can be used as a unique key or identifier in various applications.
 * The generated UUID is a random 8-character string derived from a standard UUID, ensuring uniqueness while being concise.
 * <p>
 * Example usage:
 * <pre>
 * String uniqueId = UUIDUtils.generate();
 * </pre>
 * This class is designed to be used statically, and it cannot be instantiated.
 */
@UtilityClass
public class UUIDUtils {
    /**
     * Generates a short UUID string without dashes.
     * @return A unique 8-character string derived from a standard UUID.
     */
    public static String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }
}
