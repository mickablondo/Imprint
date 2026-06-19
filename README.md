# Imprint
Encode any Java object into a compact, portable string. Decode it back anywhere, anytime.

## Public methods
- Imprint
- ImprintStore

## Imprint
### Encode
#### Simple

This method is useful for objects that can be serialized and compressed into a string.  
The compressed string can be used as a seed to recreate the original object.  
This method does not use any persistent store to keep the object, so the seed is a Base64-encoded string.

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
