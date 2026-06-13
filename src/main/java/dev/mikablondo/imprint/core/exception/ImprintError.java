package dev.mikablondo.imprint.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing specific error cases that can occur during the Imprint encoding and decoding processes.
 * Each enum constant has an associated error message that can be used when throwing an ImprintException.
 */
@AllArgsConstructor
@Getter
public enum ImprintError {
    NULL_OBJECT("Object to encode must not be null"),
    SERIALIZATION_FAILED("Failed to serialize object"),
    COMPRESSION_FAILED("Failed to compress object"),
    DECOMPRESSION_FAILED("Failed to decompress seed"),
    DESERIALIZATION_FAILED("Failed to deserialize object");

    private final String message;
}
