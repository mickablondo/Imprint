package dev.mikablondo.imprint.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * Utility class for JSON serialization and deserialization using Jackson.
 */
public class SerializationUtils {
    // configuration for the ObjectMapper to ensure compact JSON and ignore unknown properties during deserialization
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Serializes a Java object to a JSON string.
     *
     * @param o the object to serialize
     * @return the JSON byte array
     * @throws JsonProcessingException if serialization fails
     */
    public static byte[] toJson(Object o) throws JsonProcessingException {
        return mapper.writeValueAsBytes(o);
    }

    /**
     * Deserializes a JSON byte array to a Java object.
     *
     * @param json the JSON byte array to deserialize
     * @return the deserialized object
     * @throws IOException if deserialization fails
     */
    public static Object fromJson(byte[] json) throws IOException {
        return mapper.readValue(json, Object.class);
    }
}
