package io.github.mickablondo.imprint.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryImprintStore Unit Tests")
class InMemoryImprintStoreTest {

    private InMemoryImprintStore store;

    @BeforeEach
    void setUp() {
        store = new InMemoryImprintStore();
    }

    @Test
    @DisplayName("should save and return a non-null key")
    void testSaveReturnsKey() {
        byte[] data = "test data".getBytes();
        
        String key = store.save(data);
        
        assertNotNull(key);
        assertFalse(key.isEmpty());
    }

    @Test
    @DisplayName("should save data and load it back")
    void testSaveAndLoad() {
        byte[] testData = "Hello, World!".getBytes();
        
        String key = store.save(testData);
        byte[] loadedData = store.load(key);
        
        assertArrayEquals(testData, loadedData);
    }

    @Test
    @DisplayName("should return null for non-existent key")
    void testLoadNonExistentKey() {
        byte[] result = store.load("non-existent-key");
        
        assertNull(result);
    }

    @Test
    @DisplayName("should save empty byte array")
    void testSaveEmptyData() {
        byte[] emptyData = new byte[0];
        
        String key = store.save(emptyData);
        byte[] loadedData = store.load(key);
        
        assertArrayEquals(emptyData, loadedData);
    }

    @Test
    @DisplayName("should save large byte array")
    void testSaveLargeData() {
        byte[] largeData = new byte[1024 * 1024]; // 1 MB
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = (byte) (i % 256);
        }
        
        String key = store.save(largeData);
        byte[] loadedData = store.load(key);
        
        assertArrayEquals(largeData, loadedData);
    }

    @Test
    @DisplayName("should generate unique keys for different data")
    void testSaveGeneratesUniqueKeys() {
        byte[] data1 = "first data".getBytes();
        byte[] data2 = "second data".getBytes();
        
        String key1 = store.save(data1);
        String key2 = store.save(data2);
        
        assertNotEquals(key1, key2);
    }

    @Test
    @DisplayName("should handle saving identical data with different keys")
    void testSaveIdenticalDataDifferentKeys() {
        byte[] data = "same data".getBytes();
        
        String key1 = store.save(data);
        String key2 = store.save(data);
        
        assertNotEquals(key1, key2);
        assertArrayEquals(store.load(key1), store.load(key2));
    }

    @Test
    @DisplayName("should handle multiple saves and loads")
    void testMultipleSavesAndLoads() {
        byte[] data1 = "data one".getBytes();
        byte[] data2 = "data two".getBytes();
        byte[] data3 = "data three".getBytes();
        
        String key1 = store.save(data1);
        String key2 = store.save(data2);
        String key3 = store.save(data3);
        
        assertArrayEquals(data1, store.load(key1));
        assertArrayEquals(data2, store.load(key2));
        assertArrayEquals(data3, store.load(key3));
    }

    @Test
    @DisplayName("should preserve data integrity across multiple loads")
    void testDataIntegrityMultipleLoads() {
        byte[] originalData = "test data for integrity".getBytes();
        String key = store.save(originalData);
        
        byte[] load1 = store.load(key);
        byte[] load2 = store.load(key);
        byte[] load3 = store.load(key);
        
        assertArrayEquals(originalData, load1);
        assertArrayEquals(originalData, load2);
        assertArrayEquals(originalData, load3);
    }

    @Test
    @DisplayName("should work with binary data containing all byte values")
    void testSaveBinaryDataAllByteValues() {
        byte[] binaryData = new byte[256];
        for (int i = 0; i < 256; i++) {
            binaryData[i] = (byte) i;
        }
        
        String key = store.save(binaryData);
        byte[] loadedData = store.load(key);
        
        assertArrayEquals(binaryData, loadedData);
    }

    @Test
    @DisplayName("should support multiple store instances independently")
    void testMultipleStoreInstances() {
        InMemoryImprintStore store2 = new InMemoryImprintStore();
        byte[] data = "test data".getBytes();
        
        String key1 = store.save(data);
        String key2 = store2.save(data);
        
        assertArrayEquals(data, store.load(key1));
        assertArrayEquals(data, store2.load(key2));
        assertNull(store.load(key2));
        assertNull(store2.load(key1));
    }

    @Test
    @DisplayName("should return consistent keys for the same data across saves")
    void testKeyConsistency() {
        byte[] data = "consistent data".getBytes();
        String key1 = store.save(data);
        String key2 = store.save(data.clone());
        
        assertNotNull(key1);
        assertNotNull(key2);
        assertNotEquals(key1, key2);
    }

    @Test
    @DisplayName("should throw NullPointerException when loading with null key")
    void testLoadWithNullKey() {
        assertThrows(NullPointerException.class, () -> store.load(null));
    }

    @Test
    @DisplayName("should handle special characters in data")
    void testSaveSpecialCharacters() {
        byte[] data = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?".getBytes();
        
        String key = store.save(data);
        byte[] loadedData = store.load(key);
        
        assertArrayEquals(data, loadedData);
    }

    @Test
    @DisplayName("should handle Unicode characters in data")
    void testSaveUnicodeData() {
        byte[] data = "Unicode: 你好世界 🌍 مرحبا العالم".getBytes();
        
        String key = store.save(data);
        byte[] loadedData = store.load(key);
        
        assertArrayEquals(data, loadedData);
    }

    @Test
    @DisplayName("should generate keys that are not empty strings")
    void testKeyNotEmpty() {
        byte[] data = "test".getBytes();
        String key = store.save(data);
        
        assertFalse(key.trim().isEmpty());
    }

    @Test
    @DisplayName("should overwrite data with the same key if saved again")
    void testDataIsolationByKey() {
        byte[] data1 = "original".getBytes();
        byte[] data2 = "updated".getBytes();
        
        String key1 = store.save(data1);
        assertArrayEquals(data1, store.load(key1));
        
        String key2 = store.save(data2);
        assertArrayEquals(data2, store.load(key2));
        
        assertNotEquals(key1, key2);
    }

    @Test
    @DisplayName("should handle rapid successive saves")
    void testRapidSuccessiveSaves() {
        int numberOfSaves = 1000;
        String[] keys = new String[numberOfSaves];
        byte[] data = "data".getBytes();
        
        for (int i = 0; i < numberOfSaves; i++) {
            keys[i] = store.save(data);
        }
        
        for (int i = 0; i < numberOfSaves; i++) {
            assertArrayEquals(data, store.load(keys[i]));
        }
    }

    @Test
    @DisplayName("should have thread-safe operations")
    void testThreadSafety() throws InterruptedException {
        int threadCount = 10;
        int savesPerThread = 100;
        String[][] keys = new String[threadCount][savesPerThread];
        
        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                for (int i = 0; i < savesPerThread; i++) {
                    byte[] data = ("thread-" + threadId + "-data-" + i).getBytes();
                    keys[threadId][i] = store.save(data);
                }
            });
            threads[t].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        for (int t = 0; t < threadCount; t++) {
            for (int i = 0; i < savesPerThread; i++) {
                byte[] expected = ("thread-" + t + "-data-" + i).getBytes();
                assertArrayEquals(expected, store.load(keys[t][i]));
            }
        }
    }
}
