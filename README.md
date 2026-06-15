# Imprint
Encode any Java object into a compact, portable string. Decode it back anywhere, anytime.

## Public methods
- Imprint
- ImprintStore

## Imprint
### Encode
#### Simple
```
Object → sérialisation (JSON) → compression → Base64 → seed
```

#### With Store

```
Object → sérialisation (JSON) → stocké dans un store → seed = UUID court
```

Types of store :
- memory with Map
- database
- file
- Redis ?

### Decode

```
seed -> Base64 -> décompression -> désérialisation -> Object
```
