# Imprint

> Serialize Java objects into compact, portable references and restore them on-demand with minimal code.

Imprint is a lightweight, framework-agnostic library that enables seamless object serialization with flexible storage strategies. Generate compact seeds that reference complex objects, share them across systems, and reconstruct them precisely when needed.

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Architecture](#architecture)
- [Core Concepts](#core-concepts)
- [Implementation Strategies](#implementation-strategies)
- [API Reference](#api-reference)
- [Project Status](#project-status)
- [License](#license)

---

## Features

- **Flexible Serialization**: Serialize any Java object to JSON with automatic compression
- **Multiple Strategies**: Choose between embedded or store-backed encoding based on use case
- **Portable Seeds**: Generate compact, transportable string references to complex objects
- **Pluggable Storage**: Abstract storage interface with multiple implementations
- **Framework Agnostic**: Integrates with any Java framework or standalone applications
- **Production Ready**: Thread-safe, with comprehensive error handling and retry logic
- **Zero Dependencies**: Core functionality requires no external dependencies

---

## Installation

### Maven

Coming soon on Maven Central Repository!

```xml
<dependency>
    <groupId>dev.mikablondo</groupId>
    <artifactId>imprint</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Quick Start

### Embedded Strategy (Self-Contained)

Ideal for stateless, single-instance scenarios.

```java
Imprint imprint = new SelfContainedImprint();

// Encode: object is fully contained in the seed
String seed = imprint.encode(order);

// Decode: reconstruct the object from the seed
Order restored = imprint.decode(seed, Order.class);
```

### Store-Backed Strategy (Persistent)

Ideal for distributed systems and long-term state preservation.

```java
// Configure storage backend
ImprintStore store = new InMemoryImprintStore();
Imprint imprint = new StoreBackedImprint(store);

// Encode: object stored, seed contains reference key
String seed = imprint.encode(order);

// Decode: object retrieved from store
Order restored = imprint.decode(seed, Order.class);
```

---

## Architecture

### Data Flow: Encoding

#### SelfContainedImprint

```
Object → JSON Serialization → Compression → Base64 Encoding → Seed
```

#### StoreBackedImprint

```
Object → JSON Serialization → Database Store → Generated Reference Key → Seed
```

### Data Flow: Decoding

#### SelfContainedImprint (Embedded Mode)

```
Seed → Base64 Decoding → Decompression → JSON Deserialization → Object
```

#### StoreBackedImprint (Store Mode)

```
Seed → Database Lookup → JSON Deserialization → Object
```

---

## Core Concepts

### `Imprint` Interface

Primary API for serialization and deserialization operations.

**Methods:**

- `String encode(T object)` — Serializes an object and returns a seed
- `T decode(String seed, Class<T> type)` — Deserializes an object from a seed

### `ImprintStore` Interface

Storage abstraction for persisting serialized object data.

**Methods:**

- `String save(byte[] data)` — Persists binary data and returns a unique identifier
- `byte[] load(String key)` — Retrieves binary data by identifier

**Available Implementations:**


| Implementation           | Storage             | Use Case                                     |
| ------------------------ | ------------------- | -------------------------------------------- |
| **SelfContainedImprint** | Embedded in seed    | Single-instance, stateless operations        |
| **InMemoryImprintStore** | JVM heap            | Development, testing, temporary state        |
| **JdbcImprintStore**     | Relational database | Production, distributed systems, persistence |
| **FileImprintStore**     | File system         | Planned                                      |
| **RedisImprintStore**    | Redis cache         | Planned                                      |

---

## Implementation Strategies

### 1. SelfContainedImprint

**Best for:** Stateless operations, single-instance deployments, scenarios without external dependencies.

**Characteristics:**

- No external storage required
- Complete object data embedded in seed
- Seed size grows with object complexity
- Ideal for sharing across networks

**Example:**

```java
public String encodeOrder(Order order) {
    Imprint imprint = new SelfContainedImprint();
    return imprint.encode(order);  // Seed contains full order data
}

public Order decodeOrder(String seed) {
    Imprint imprint = new SelfContainedImprint();
    return imprint.decode(seed, Order.class);
}
```

**Considerations:**

- Seed size limitations may apply in some contexts (URLs, message brokers)
- Objects must be serializable to JSON
- No server-side state management

---

### 2. InMemoryImprintStore

**Best for:** Development, testing, and temporary state management.

**Characteristics:**

- Data persisted in JVM memory
- Fast access patterns
- Data lost on application restart
- Thread-safe with concurrent collections

**Example:**

```java
public class OrderServiceWithMemoryStore {
    private final ImprintStore store = new InMemoryImprintStore();
    private final Imprint imprint = new StoreBackedImprint(store);
  
    public String encodeOrder(Order order) {
        return imprint.encode(order);  // Seed contains only reference key
    }
}
```

**Considerations:**

- Not suitable for production persistent storage
- Memory footprint grows with stored objects
- Single-instance only (not distributed)

---

### 3. JdbcImprintStore

**Best for:** Production deployments requiring persistent, distributed storage.

#### Overview

`JdbcImprintStore` is a production-grade implementation that persists serialized objects in a relational database. Objects are stored efficiently, and seeds contain only compact reference keys (8-character UUIDs).

#### Database Setup

Create the required table in your database:

```sql
CREATE TABLE imprint_store (
    id VARCHAR(8) PRIMARY KEY,
    data BYTEA NOT NULL
);
```

**Database-Specific Types:**

- **PostgreSQL**: `BYTEA`
- **MySQL**: `LONGBLOB`
- **Oracle**: `BLOB`
- **SQL Server**: `VARBINARY(MAX)`

#### Configuration

```java
// Step 1: Configure DataSource
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:postgresql://localhost:5432/imprint_db");
config.setUsername("user");
config.setPassword("password");
config.setMaximumPoolSize(10);
DataSource dataSource = new HikariDataSource(config);

// Step 2: Create store
ImprintStore store = new JdbcImprintStore(dataSource);

// Step 3: Use with StoreBackedImprint
Imprint imprint = new StoreBackedImprint(store);
```

#### Production Example

```java
@Service
public class OrderService {
    private final Imprint imprint;
  
    public OrderService(DataSource dataSource) {
        ImprintStore store = new JdbcImprintStore(dataSource);
        this.imprint = new StoreBackedImprint(store);
    }
  
    /**
     * Generates a compact reference for an order
     * @param order the order to reference
     * @return a compact seed containing only the reference key
     */
    public String createOrderReference(Order order) {
        return imprint.encode(order);
    }
  
    /**
     * Retrieves an order from its reference
     * @param reference the seed generated by createOrderReference
     * @return the original order object
     * @throws ImprintException if the reference is invalid or data is corrupted
     */
    public Order getOrder(String reference) {
        return imprint.decode(reference, Order.class);
    }
}
```

#### Implementation Details


| Aspect                  | Details                                                           |
| ----------------------- | ----------------------------------------------------------------- |
| **Concurrency**         | Thread-safe; leverages connection pooling from`DataSource`        |
| **Collision Handling**  | Automatic retry logic (5 attempts) for UUID collisions            |
| **Error Handling**      | Throws`ImprintException` with descriptive error codes on failures |
| **Resource Management** | Properly manages JDBC connections via try-with-resources pattern  |
| **Data Integrity**      | Binary data is compressed before storage to minimize space        |

#### Performance Considerations


| Metric                | Consideration                                                          |
| --------------------- | ---------------------------------------------------------------------- |
| **I/O Latency**       | Each`save()` and `load()` operation incurs a database round-trip       |
| **Throughput**        | Scalability depends on database configuration and connection pool size |
| **Storage Footprint** | Compressed binary storage; requirements scale with object complexity   |
| **Optimization**      | Use connection pooling, tune database indexes on`imprint_store.id`     |

---

## API Reference

### SelfContainedImprint

```java
Imprint imprint = new SelfContainedImprint();

// Encode
String seed = imprint.encode(object);

// Decode
MyObject restored = imprint.decode(seed, MyObject.class);
```

### StoreBackedImprint

```java
ImprintStore store = new InMemoryImprintStore();
Imprint imprint = new StoreBackedImprint(store);

// Encode
String seed = imprint.encode(object);

// Decode
MyObject restored = imprint.decode(seed, MyObject.class);
```

### JdbcImprintStore

```java
DataSource dataSource = /* configured DataSource */;
ImprintStore store = new JdbcImprintStore(dataSource);

// Used via StoreBackedImprint
Imprint imprint = new StoreBackedImprint(store);
```

---


## Project Status

### Current Release


| Component                                | Status     |
| ---------------------------------------- | ---------- |
| SelfContainedImprint (Embedded Encoding) | ✅ Stable  |
| StoreBackedImprint API                   | ✅ Stable  |
| InMemoryImprintStore                     | ✅ Stable  |
| JdbcImprintStore                         | ✅ Stable  |
| FileImprintStore                         | 🚧 Planned |
| RedisImprintStore                        | 🚧 Planned |

---

## License

MIT License

**Copyright © 2026 Mikablondo**

---

## Support

For issues, feature requests, or questions, please refer to the project repository.
