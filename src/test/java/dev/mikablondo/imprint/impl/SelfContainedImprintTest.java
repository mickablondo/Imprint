package dev.mikablondo.imprint.impl;

import dev.mikablondo.imprint.Imprint;
import dev.mikablondo.imprint.core.exception.ImprintException;
import dev.mikablondo.imprint.model.Person;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ImprintSimple implementation of the Imprint interface.
 * This class will contain unit tests to verify the correctness of the encoding and decoding processes of the ImprintSimple class, ensuring that objects are properly serialized, compressed, Base64 encoded, and that they can be accurately decoded back to their original form.
 */
class SelfContainedImprintTest {

    private final Imprint imprint = new SelfContainedImprint();
    private final Person person = new Person(
            "Jean", "Dupont", 42, "jean@example.com", List.of("chess", "hiking")
    );

    //region ENCODE
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
    //endregion

    //region DECODE
    @Test
    void shouldDecodeEncodedObjectToOriginal() {
        final String seed = imprint.encode(person);
        final var decoded = imprint.decode(seed, Person.class);
        assertEquals(person, decoded);
    }

    @Test
    void shouldThrowImprintExceptionWhenDecodingToDifferentType() {
        final String seed = imprint.encode(person);
        assertThrows(ImprintException.class, () -> imprint.decode(seed, String.class));
    }

    @Test
    void shouldThrowImprintExceptionWhenDecodingCorruptedSeed() {
        final String seed = imprint.encode(person);
        byte[] decoded = Base64.getDecoder().decode(seed);
        // corrupt one byte to keep Base64 valid but break decompression/deserialization
        decoded[0] = (byte) (decoded[0] ^ 0xFF);
        final String tampered = Base64.getEncoder().encodeToString(decoded);
        assertThrows(ImprintException.class, () -> imprint.decode(tampered, Person.class));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDecodingInvalidBase64() {
        final String invalidSeed = "not-a-valid-base64!";
        assertThrows(IllegalArgumentException.class, () -> imprint.decode(invalidSeed, Person.class));
    }
    //endregion
}
