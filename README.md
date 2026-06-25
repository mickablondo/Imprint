# Imprint

Encode any Java object into a compact, portable string and restore it later with a single line of code.

Imprint provides two encoding strategies depending on your needs:
- Self-contained encoding (no external dependency)
- Store-backed encoding (external storage required)

## Features

- Serialize Java objects to JSON
- Compress payloads
- Generate portable string seeds
- Restore objects from seeds
- Pluggable storage backends
- Framework agnostic

## Getting Started

### Installation

Coming soon on Maven Central!

### Quick Start

```java
// Encode an object
Imprint imprint = new SelfContainedImprint();
String seed = imprint.encode(new MyObject());

// Decode it later
MyObject restored = imprint.decode(seed, MyObject.class);
```

## Core APIs

### `Imprint`

Main entry point for encoding and decoding objects.

### `ImprintStore`

Storage abstraction used when objects should be persisted outside the seed.

Available implementations:

- In-memory store
- JDBC store
- File store (planned)
- Redis store (planned)

---

## Encoding

### SelfContainedImprint

The object is fully contained in the generated seed.

No external storage is required.

```text
Object
  ↓
JSON serialization
  ↓
Compression
  ↓
Base64 encoding
  ↓
Seed
```

Example:

```java
Imprint imprint = new SelfContainedImprint();

String seed = imprint.encode(order);

Order restored = imprint.decode(seed, Order.class);
```

### StoreBackedImprint

The object is stored in an external `ImprintStore`.

The seed only contains a generated key.

```text
Object
  ↓
JSON serialization
  ↓
Store
  ↓
Generated key
  ↓
Seed
```

Example:

```java
ImprintStore store = new InMemoryImprintStore();

Imprint imprint = new StoreBackedImprint(store);

String seed = imprint.encode(order);

Order restored = imprint.decode(seed, Order.class);
```

## Decoding

### Embedded Mode

```text
Seed
  ↓
Base64 decoding
  ↓
Decompression
  ↓
JSON deserialization
  ↓
Object
```

### Store Mode

```text
Seed
  ↓
Lookup in store
  ↓
JSON deserialization
  ↓
Object
```

## Roadmap

Current status:

- ✅ Embedded mode
- ✅ In-memory store
- ✅ JDBC store
- 🚧 File store

## License

MIT License
