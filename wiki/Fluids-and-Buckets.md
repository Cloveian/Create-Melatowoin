# Fluids and Buckets

The mod registers four fluids, each with still and flowing variants, a fluid block, and a bucket item.

| Fluid ID | Block | Bucket | Used By |
|----------|-------|--------|---------|
| `melatowoin:acetone` | Acetone (block) | Acetone Bucket | Chloroform recipe |
| `melatowoin:bleach` | Bleach (block) | Bleach Bucket | Chloroform recipe |
| `melatowoin:chloroform` | Chloroform (block) | Chloroform Bucket | Cyan Pill, Throwable Cyan Sauce |
| `melatowoin:vinegar` | Vinegar (block) | Vinegar Bucket | Calcium Acetate recipe |

All four are full Create-compatible: pipes, tanks, mixers, compactors, and basins all accept them.

## Implementation notes

The fluid declarations live in the platform modules (`fabric/.../fluid/` and `forge/.../fluid/`), since Forge and Fabric use different fluid APIs. The common `ModFluids` class only holds the registry suppliers, which the platform modules populate before calling `register()`.

See [[Architecture]] for more.

## Where they come from

| Fluid | Production |
|-------|-----------|
| Vinegar | Heated mixer: bone meal + apple + water |
| Acetone | Superheated mixer: Calcium Acetate |
| Bleach | Heated mixer: Furrycore + water (along with Unpure Furrynes) |
| Chloroform | Mixer: Bleach + Acetone (→ Chloroform + Water) |

Full chain: [[Chloroform-Chain]].
