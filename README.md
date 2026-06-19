# Imprint

Encode any Java object into a compact, portable string and restore it later with a single line of code.

```java
String seed = imprint.encode(order);

Order restored = imprint.decode(seed, Order.class);
```

## Features

- Serialize Java objects to JSON
- Compress payloads
- Generate portable string seeds
- Restore objects from seeds
- Pluggable storage backends
- Framework agnostic

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

### Embedded Mode

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
String seed = imprint.encode(myObject);
```

Recommended for:

- URLs
- Temporary sharing
- Stateless applications
- Small to medium payloads

### Store Mode

The object is persisted in a storage backend.

The generated seed only contains a unique identifier.

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

String seed = imprint.encode(myObject, store);
```

Recommended for:

- Large payloads
- Long-term persistence
- Distributed systems
- Shared storage

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

Example:

```java
MyObject object = imprint.decode(seed, MyObject.class);
```

## Roadmap

Current status:

- ✅ Embedded mode
- ✅ In-memory store
- 🚧 JDBC store
- 📋 File store

## License

MIT License
