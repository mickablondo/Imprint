package io.github.mickablondo.imprint.impl;

import io.github.mickablondo.imprint.Imprint;
import io.github.mickablondo.imprint.ImprintStore;
import io.github.mickablondo.imprint.model.Person;
import io.github.mickablondo.imprint.store.InMemoryImprintStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreBackedImprint Batch Operations Tests")
class StoreBackedImprintBatchTest {

    private Imprint imprint;
    private ImprintStore store;

    @BeforeEach
    void setUp() {
        store = new InMemoryImprintStore();
        imprint = new StoreBackedImprint(store);
    }

    @Test
    @DisplayName("should encode all objects in a list")
    void shouldEncodeAll() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of("reading"));
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of("gaming"));
        final Person person3 = new Person("Carol", "Brown", 35, "carol@example.com", List.of("sports"));
        final List<Person> people = List.of(person1, person2, person3);

        final List<String> seeds = imprint.encodeAll(people);

        assertEquals(3, seeds.size());
        assertTrue(seeds.stream().allMatch(seed -> seed != null && !seed.isBlank()));
    }

    @Test
    @DisplayName("should decode all seeds to original objects")
    void shouldDecodeAll() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of("reading"));
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of("gaming"));
        final Person person3 = new Person("Carol", "Brown", 35, "carol@example.com", List.of("sports"));
        final List<Person> people = List.of(person1, person2, person3);

        final List<String> seeds = imprint.encodeAll(people);
        final List<Person> decoded = imprint.decodeAll(seeds, Person.class);

        assertEquals(3, decoded.size());
        assertEquals(person1, decoded.get(0));
        assertEquals(person2, decoded.get(1));
        assertEquals(person3, decoded.get(2));
    }

    @Test
    @DisplayName("should preserve order when encoding and decoding all")
    void shouldPreserveOrderInBatchOperations() {
        final Person person1 = new Person("Zoe", "Alpha", 20, "zoe@example.com", List.of());
        final Person person2 = new Person("Alice", "Beta", 25, "alice@example.com", List.of());
        final Person person3 = new Person("Bob", "Gamma", 30, "bob@example.com", List.of());
        final Person person4 = new Person("Carol", "Delta", 35, "carol@example.com", List.of());
        final List<Person> people = List.of(person1, person2, person3, person4);

        final List<String> seeds = imprint.encodeAll(people);
        final List<Person> decoded = imprint.decodeAll(seeds, Person.class);

        assertEquals(4, decoded.size());
        assertEquals("Zoe", decoded.get(0).firstName());
        assertEquals("Alice", decoded.get(1).firstName());
        assertEquals("Bob", decoded.get(2).firstName());
        assertEquals("Carol", decoded.get(3).firstName());
    }

    @Test
    @DisplayName("should handle empty list in encodeAll")
    void shouldHandleEmptyListInEncodeAll() {
        final List<String> seeds = imprint.encodeAll(List.of());
        assertEquals(0, seeds.size());
    }

    @Test
    @DisplayName("should handle empty list in decodeAll")
    void shouldHandleEmptyListInDecodeAll() {
        final List<Person> decoded = imprint.decodeAll(List.of(), Person.class);
        assertEquals(0, decoded.size());
    }

    @Test
    @DisplayName("should encode and decode single element list")
    void shouldHandleSingleElementList() {
        final Person person = new Person("John", "Doe", 40, "john@example.com", List.of("coding"));
        final List<Person> people = List.of(person);

        final List<String> seeds = imprint.encodeAll(people);
        final List<Person> decoded = imprint.decodeAll(seeds, Person.class);

        assertEquals(1, decoded.size());
        assertEquals(person, decoded.get(0));
    }

    @Test
    @DisplayName("should generate different seeds for each object")
    void shouldGenerateDifferentSeedsForEachObject() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of());
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of());
        final List<Person> people = List.of(person1, person2);

        final List<String> seeds = imprint.encodeAll(people);

        assertEquals(2, seeds.size());
        assertNotEquals(seeds.get(0), seeds.get(1));
    }

    @Test
    @DisplayName("should generate different seeds for same object encoded at different times")
    void shouldGenerateDifferentSeedsForSameObjectsDifferentTimes() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of());

        final String seed1 = imprint.encode(person1);
        final String seed2 = imprint.encode(person1);

        // StoreBackedImprint generates unique UUIDs each time, so seeds should be different
        assertNotEquals(seed1, seed2);

        // But they should decode to the same person
        final Person decoded1 = imprint.decode(seed1, Person.class);
        final Person decoded2 = imprint.decode(seed2, Person.class);
        assertEquals(decoded1, decoded2);
        assertEquals(decoded1, person1);
    }

    @Test
    @DisplayName("should handle large batch operations")
    void shouldHandleLargeBatch() {
        final List<Person> people = List.of(
                new Person("Alice" + 0, "Smith", 30, "alice0@example.com", List.of()),
                new Person("Alice" + 1, "Smith", 30, "alice1@example.com", List.of()),
                new Person("Alice" + 2, "Smith", 30, "alice2@example.com", List.of()),
                new Person("Alice" + 3, "Smith", 30, "alice3@example.com", List.of()),
                new Person("Alice" + 4, "Smith", 30, "alice4@example.com", List.of())
        );

        final List<String> seeds = imprint.encodeAll(people);
        final List<Person> decoded = imprint.decodeAll(seeds, Person.class);

        assertEquals(5, decoded.size());
        for (int i = 0; i < 5; i++) {
            assertEquals("Alice" + i, decoded.get(i).firstName());
        }
    }

    @Test
    @DisplayName("should use same store for batch operations")
    void shouldUseStoredDataInBatch() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of());
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of());
        final List<Person> people = List.of(person1, person2);

        final List<String> seeds = imprint.encodeAll(people);

        // Seeds should be valid keys in the store
        assertTrue(seeds.stream().allMatch(seed -> store.load(seed) != null));
    }

    @Test
    @DisplayName("should mix single and batch operations")
    void shouldMixSingleAndBatchOperations() {
        final Person person1 = new Person("Alice", "Smith", 30, "alice@example.com", List.of());
        final Person person2 = new Person("Bob", "Jones", 25, "bob@example.com", List.of());
        final Person person3 = new Person("Carol", "Brown", 35, "carol@example.com", List.of());

        final String seed1 = imprint.encode(person1);
        final List<String> seeds = imprint.encodeAll(List.of(person2, person3));

        final Person decoded1 = imprint.decode(seed1, Person.class);
        final List<Person> decoded23 = imprint.decodeAll(seeds, Person.class);

        assertEquals(person1, decoded1);
        assertEquals(2, decoded23.size());
        assertEquals(person2, decoded23.get(0));
        assertEquals(person3, decoded23.get(1));
    }
}
