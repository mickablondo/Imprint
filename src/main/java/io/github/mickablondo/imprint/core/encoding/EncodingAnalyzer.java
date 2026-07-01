package io.github.mickablondo.imprint.core.encoding;

import io.github.mickablondo.imprint.core.exception.ImprintError;
import io.github.mickablondo.imprint.core.exception.ImprintException;
import io.github.mickablondo.imprint.core.utils.Base64Utils;
import io.github.mickablondo.imprint.core.utils.CompressionUtils;
import lombok.experimental.UtilityClass;

import java.io.IOException;

/**
 * Utility class for analyzing encoding metrics of serialized objects.
 * Provides detailed information about object sizes at each stage of the encoding pipeline.
 */
@UtilityClass
public class EncodingAnalyzer {

    /**
     * Analyzes the encoding metrics of an already-encoded seed.
     * Extracts size information without requiring the original object.
     *
     * @param seed the Base64-encoded seed to analyze
     * @return EncodingMetadata containing size metrics and compression ratio
     * @throws ImprintException if the seed cannot be decoded or decompressed
     */
    public static EncodingMetadata analyze(String seed) {
        try {
            // Step 1: Base64 decode
            int encodedSize = seed.length();
            byte[] compressedBytes = Base64Utils.decode(seed);
            int compressedSize = compressedBytes.length;

            // Step 2: Decompress
            byte[] jsonBytes = CompressionUtils.decompress(compressedBytes);
            int jsonSize = jsonBytes.length;

            // Step 3: Calculate compression ratio
            double compressionRatio = (double) compressedSize / jsonSize;

            return new EncodingMetadata(jsonSize, compressedSize, encodedSize, compressionRatio);
        } catch (IllegalArgumentException e) {
            throw new ImprintException(ImprintError.INVALID_BASE64_SEED);
        } catch (IOException e) {
            throw new ImprintException(ImprintError.DECOMPRESSION_FAILED);
        }
    }
}
