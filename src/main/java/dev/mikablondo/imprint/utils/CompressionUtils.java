package dev.mikablondo.imprint.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compression utility class for compressing and decompressing strings using GZIP.
 */
public class CompressionUtils {
    /**
     * Compresses a byte array using GZIP.
     *
     * @param seed the byte array to compress
     * @return the compressed byte array
     */
    public static byte[] compress(byte[] seed) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(seed);
            gzip.finish();
            return bos.toByteArray();
        }
    }

    /**
     * Decompresses a GZIP-compressed byte array.
     *
     * @param seed the GZIP-compressed byte array
     * @return the decompressed byte array
     */
    public static byte[] decompress(byte[] seed) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(seed))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzip.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }
}
