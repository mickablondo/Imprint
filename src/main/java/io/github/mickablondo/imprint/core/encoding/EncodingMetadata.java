package io.github.mickablondo.imprint.core.encoding;

/**
 * Record containing metrics about the encoding process of an object.
 *
 * @param jsonSize the size of the object in JSON format (before compression)
 * @param compressedSize the size after compression (e.g., GZIP)
 * @param encodedSize the size after final encoding (e.g., Base64)
 * @param compressionRatio the ratio of compressed size to JSON size (compressedSize / jsonSize)
 */
public record EncodingMetadata(
        int jsonSize,
        int compressedSize,
        int encodedSize,
        double compressionRatio
) {}
