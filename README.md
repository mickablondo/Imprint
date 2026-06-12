# Imprint
Encode any Java object into a compact, portable string. Decode it back anywhere, anytime.

## Encode

```
Object → sérialisation (JSON) → compression → Base64 → seed
```

### With Store

```
Object → sérialisation (JSON) → stocké dans Redis/Map → seed = UUID court
```

## Decode

```
seed -> Base64 -> décompression -> désérialisation -> Object
```