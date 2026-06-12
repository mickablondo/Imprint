package dev.mikablondo.imprint.impl;

import dev.mikablondo.imprint.Imprint;
import dev.mikablondo.imprint.exception.ImprintException;
import dev.mikablondo.imprint.model.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ImprintSimple implementation of the Imprint interface.
 * This class will contain unit tests to verify the correctness of the encoding and decoding processes of the ImprintSimple class, ensuring that objects are properly serialized, compressed, Base64 encoded, and that they can be accurately decoded back to their original form.
 */
class ImprintSimpleTest {

    private final Imprint imprint = new ImprintSimple();
    private final Person person = new Person(
            "Jean", "Dupont", 42, "jean@example.com", List.of("chess", "hiking")
    );

    @Test
    void shouldEncodeObject() {
        final String seed = imprint.encode(person);
        assertNotNull(seed);
        assertFalse(seed.isBlank());
    }

    @Test
    void shouldGenerateSameSeedsWhenEncode() {
        final String seed1 = imprint.encode(person);
        final String seed2 = imprint.encode(person);

        assertNotNull(seed1);
        assertFalse(seed1.isBlank());
        assertNotNull(seed2);
        assertFalse(seed2.isBlank());
        assertEquals(seed1, seed2);
    }

    @Test
    void shouldGenerateDifferentSeedsWhenEncode() {
        final String seed1 = imprint.encode(person);
        final String seed2 = imprint.encode("test");

        assertNotNull(seed1);
        assertFalse(seed1.isBlank());
        assertNotNull(seed2);
        assertFalse(seed2.isBlank());
        assertNotEquals(seed1, seed2);
    }

    @Test
    void shouldThrowExceptionWhenEncodingNull() {
        assertThrows(ImprintException.class, () -> imprint.encode(null));
    }
}
