package io.github.mickablondo.imprint.store;

import io.github.mickablondo.imprint.core.exception.ImprintException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileImprintStore Tests")
class FileImprintStoreTest {

    @TempDir
    Path tempDir;

    private FileImprintStore store;

    @BeforeEach
    void setup() {
        store = new FileImprintStore(tempDir.toString());
    }

    @Test
    @DisplayName("should save data and return a unique key")
    void testSaveReturnsUniqueKey() {
        // Arrange
        byte[] data = "test data".getBytes();

        // Act
        String key1 = store.save(data);
        String key2 = store.save(data);

        // Assert
        assertNotNull(key1);
        assertNotNull(key2);
        assertNotEquals(key1, key2);
        assertEquals(8, key1.length());
        assertEquals(8, key2.length());
    }

    @Test
    @DisplayName("should persist data to file system")
    void testSaveWritesDataToFile() {
        // Arrange
        byte[] data = "test content".getBytes();

        // Act
        String key = store.save(data);

        // Assert
        Path filePath = tempDir.resolve(key);
        assertTrue(Files.exists(filePath));
    }

    @Test
    @DisplayName("should load data previously saved")
    void testLoadReturnsCorrectData() {
        // Arrange
        byte[] originalData = "test data for loading".getBytes();
        String key = store.save(originalData);

        // Act
        byte[] loadedData = store.load(key);

        // Assert
        assertArrayEquals(originalData, loadedData);
    }

    @Test
    @DisplayName("should throw ImprintException when loading non-existent key")
    void testLoadThrowsExceptionForMissingKey() {
        // Act & Assert
        ImprintException exception = assertThrows(ImprintException.class, () ->
            store.load("nonexistent")
        );
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("should create directories if they don't exist")
    void testSaveCreatesDirectories() {
        // Arrange
        Path newDir = tempDir.resolve("subdir1").resolve("subdir2");
        FileImprintStore storeWithNestedPath = new FileImprintStore(newDir.toString());
        byte[] data = "test".getBytes();

        // Act
        String key = storeWithNestedPath.save(data);

        // Assert
        assertTrue(Files.exists(newDir.resolve(key)));
    }

    @Test
    @DisplayName("should handle large data correctly")
    void testSaveAndLoadLargeData() {
        // Arrange
        byte[] largeData = new byte[1024 * 100]; // 100KB
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = (byte) (i % 256);
        }

        // Act
        String key = store.save(largeData);
        byte[] loadedData = store.load(key);

        // Assert
        assertArrayEquals(largeData, loadedData);
    }

    @Test
    @DisplayName("should handle empty data")
    void testSaveAndLoadEmptyData() {
        // Arrange
        byte[] emptyData = new byte[0];

        // Act
        String key = store.save(emptyData);
        byte[] loadedData = store.load(key);

        // Assert
        assertArrayEquals(emptyData, loadedData);
    }

    @Test
    @DisplayName("should save multiple files independently")
    void testMultipleSavesAreIndependent() {
        // Arrange
        byte[] data1 = "first".getBytes();
        byte[] data2 = "second".getBytes();
        byte[] data3 = "third".getBytes();

        // Act
        String key1 = store.save(data1);
        String key2 = store.save(data2);
        String key3 = store.save(data3);

        // Assert
        assertArrayEquals(data1, store.load(key1));
        assertArrayEquals(data2, store.load(key2));
        assertArrayEquals(data3, store.load(key3));
    }

    @Test
    @DisplayName("should throw ImprintException on IO error during load")
    void testLoadHandlesIOError() throws Exception {
        // Arrange
        byte[] data = "test".getBytes();
        String key = store.save(data);
        Path filePath = tempDir.resolve(key);
        
        // Delete the file to trigger IO error
        Files.delete(filePath);

        // Act & Assert
        ImprintException exception = assertThrows(ImprintException.class, () ->
            store.load(key)
        );
        assertTrue(exception.getMessage().contains("not found"));
    }
}
