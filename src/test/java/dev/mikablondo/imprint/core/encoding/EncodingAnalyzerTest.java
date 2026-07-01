package dev.mikablondo.imprint.core.encoding;

import dev.mikablondo.imprint.core.utils.Base64Utils;
import dev.mikablondo.imprint.core.utils.CompressionUtils;
import dev.mikablondo.imprint.core.utils.SerializationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EncodingAnalyzer Tests")
class EncodingAnalyzerTest {

    static class TestObject {
        public String name;
        public int value;
        public String description;

        TestObject(String name, int value, String description) {
            this.name = name;
            this.value = value;
            this.description = description;
        }
    }

    @Test
    @DisplayName("should analyze a valid seed and return correct metrics")
    void testAnalyzeSeedWithValidData() throws Exception {
        // Arrange
        TestObject testObj = new TestObject("Test", 42, "This is a test object for encoding analysis");
        byte[] jsonBytes = SerializationUtils.toJson(testObj);
        byte[] compressedBytes = CompressionUtils.compress(jsonBytes);
        String seed = Base64Utils.encode(compressedBytes);

        // Act
        EncodingMetadata metrics = EncodingAnalyzer.analyze(seed);

        // Assert
        assertEquals(jsonBytes.length, metrics.jsonSize());
        assertEquals(compressedBytes.length, metrics.compressedSize());
        assertEquals(seed.length(), metrics.encodedSize());
        assertTrue(metrics.compressionRatio() > 0);
    }

    @Test
    @DisplayName("should calculate compression ratio correctly")
    void testCompressionRatioCalculation() throws Exception {
        // Arrange
        TestObject testObj = new TestObject("Test", 100, "Description");
        byte[] jsonBytes = SerializationUtils.toJson(testObj);
        byte[] compressedBytes = CompressionUtils.compress(jsonBytes);
        String seed = Base64Utils.encode(compressedBytes);

        // Act
        EncodingMetadata metrics = EncodingAnalyzer.analyze(seed);
        double expectedRatio = (double) compressedBytes.length / jsonBytes.length;

        // Assert
        assertEquals(expectedRatio, metrics.compressionRatio(), 0.0001);
    }

    @Test
    @DisplayName("should throw exception for invalid Base64 seed")
    void testAnalyzeWithInvalidBase64() {
        // Arrange
        String invalidSeed = "!!!invalid base64!!!";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            EncodingAnalyzer.analyze(invalidSeed)
        );
        assertTrue(exception.getMessage().contains("Failed to decode Base64 seed"));
    }

    @Test
    @DisplayName("should throw exception for corrupted compressed data")
    void testAnalyzeWithCorruptedData() {
        // Arrange
        String corruptedSeed = Base64Utils.encode("corrupted data".getBytes());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            EncodingAnalyzer.analyze(corruptedSeed)
        );
        assertTrue(exception.getMessage().contains("Failed to decompress seed data"));
    }

    @Test
    @DisplayName("should handle large objects correctly")
    void testAnalyzeLargeObject() throws Exception {
        // Arrange
        StringBuilder largeDescription = new StringBuilder();
        largeDescription.repeat("This is a large object used for testing compression efficiency. ", 1000);
        TestObject testObj = new TestObject("Large", 999, largeDescription.toString());
        byte[] jsonBytes = SerializationUtils.toJson(testObj);
        byte[] compressedBytes = CompressionUtils.compress(jsonBytes);
        String seed = Base64Utils.encode(compressedBytes);

        // Act
        EncodingMetadata metrics = EncodingAnalyzer.analyze(seed);

        // Assert
        assertEquals(jsonBytes.length, metrics.jsonSize());
        assertEquals(compressedBytes.length, metrics.compressedSize());
        assertTrue(metrics.compressionRatio() < 1.0, "Compression should be effective on large repetitive data");
    }

    @Test
    @DisplayName("should show compression inefficiency for small objects")
    void testSmallObjectCompressionRatio() throws Exception {
        // Arrange
        TestObject testObj = new TestObject("X", 1, "Y");
        byte[] jsonBytes = SerializationUtils.toJson(testObj);
        byte[] compressedBytes = CompressionUtils.compress(jsonBytes);
        String seed = Base64Utils.encode(compressedBytes);

        // Act
        EncodingMetadata metrics = EncodingAnalyzer.analyze(seed);

        // Assert
        // Small objects may have poor compression due to GZIP overhead
        assertTrue(metrics.compressionRatio() >= 0.8, "Small object compression ratio should be high (poor compression)");
    }

    @Test
    @DisplayName("should maintain accurate size metrics through encoding pipeline")
    void testSizeMetricsAccuracy() throws Exception {
        // Arrange
        TestObject testObj = new TestObject("Metrics", 12345, "Testing size accuracy through the pipeline");
        byte[] jsonBytes = SerializationUtils.toJson(testObj);
        int expectedJsonSize = jsonBytes.length;
        
        byte[] compressedBytes = CompressionUtils.compress(jsonBytes);
        int expectedCompressedSize = compressedBytes.length;
        
        String seed = Base64Utils.encode(compressedBytes);
        int expectedEncodedSize = seed.length();

        // Act
        EncodingMetadata metrics = EncodingAnalyzer.analyze(seed);

        // Assert
        assertEquals(expectedJsonSize, metrics.jsonSize());
        assertEquals(expectedCompressedSize, metrics.compressedSize());
        assertEquals(expectedEncodedSize, metrics.encodedSize());
    }

    @Test
    @DisplayName("should handle empty seed gracefully")
    void testAnalyzeEmptySeed() {
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            EncodingAnalyzer.analyze("")
        );
    }
}
