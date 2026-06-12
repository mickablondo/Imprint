package dev.mikablondo.imprint.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
     * @return the JSON string
     * @throws Exception if serialization fails
     */
    public static String toJson(Object o) throws Exception {
        return mapper.writeValueAsString(o);
    }

    /**
     * Deserializes a JSON string to a Java object.
     *
     * @param json the JSON string to deserialize
     * @return the deserialized object
     * @throws Exception if deserialization fails
     */
    public static Object fromJson(String json) throws Exception {
        return mapper.readValue(json, Object.class);
    }
}
