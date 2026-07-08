package io.github.mickablondo.imprint.impl;

import io.github.mickablondo.imprint.Imprint;
import io.github.mickablondo.imprint.core.exception.ImprintException;
import io.github.mickablondo.imprint.model.Person;
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

    //region BATCH OPERATIONS
    @Test
    void shouldEncodeAllObjects() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of("reading"));
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of("gaming"));
        final List<Person> people = List.of(person1, person2);

        final List<String> seeds = imprint.encodeAll(people);

        assertEquals(2, seeds.size());
        assertTrue(seeds.stream().allMatch(seed -> seed != null && !seed.isBlank()));
    }

    @Test
    void shouldDecodeAllSeeds() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of("reading"));
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of("gaming"));
        final List<Person> people = List.of(person1, person2);

        final List<String> seeds = imprint.encodeAll(people);
        final List<Person> decoded = imprint.decodeAll(seeds, Person.class);

        assertEquals(2, decoded.size());
        assertEquals(person1, decoded.get(0));
        assertEquals(person2, decoded.get(1));
    }

    @Test
    void shouldEncodeAllEmptyList() {
        final List<String> seeds = imprint.encodeAll(List.of());
        assertEquals(0, seeds.size());
    }

    @Test
    void shouldDecodeAllEmptyList() {
        final List<Person> decoded = imprint.decodeAll(List.of(), Person.class);
        assertEquals(0, decoded.size());
    }

    @Test
    void shouldPreserveOrderWhenEncodingAndDecodingAll() {
        final Person person1 = new Person("Zoe", "Alpha", 20, "zoe@example.com", List.of());
        final Person person2 = new Person("Alice", "Beta", 25, "alice@example.com", List.of());
        final Person person3 = new Person("Bob", "Gamma", 30, "bob@example.com", List.of());
        final List<Person> people = List.of(person1, person2, person3);

        final List<String> seeds = imprint.encodeAll(people);
        final List<Person> decoded = imprint.decodeAll(seeds, Person.class);

        assertEquals(3, decoded.size());
        assertEquals("Zoe", decoded.get(0).firstName());
        assertEquals("Alice", decoded.get(1).firstName());
        assertEquals("Bob", decoded.get(2).firstName());
    }
    //endregion
}
