# Dyeing System

The cat wearables and the Orange Pill all support **two-color dyeing** in a crafting table. The system is a custom recipe — no anvil, no cauldron, just dyes on a grid.

## How to Dye

1. Place the item anywhere in the crafting grid.
2. Place dyes in slots **adjacent** to it (horizontally or vertically).
3. Dyes on the **same row** mix into the **main color** (the fur/body color).
4. Dyes on the **same column** mix into the **accent color** (inner ear / paw pad).
5. Mixing follows the vanilla Java Edition leather-armor formula. Multiple dyes on the same side are blended into one result color.

Any non-dye item in the grid (other than the equipment piece itself) causes the recipe to fail.

## Layout Example

```
. D .       D = "above" → mixed into accent
D X D       X = wearable piece
. D .       D = "below" → also accent
            left/right of X → main
```

Example below: **Cat Ears** + 1× Blue Dye above + 1× Orange Dye to the left → Cat Ears with blue accent and orange main color.

![Dyeing Cat Ears with orange (main) and blue (accent)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/dye-crafting.png)

## Defaults and the "Dyed" Flag

Each piece tracks two flags: `MainDyed` and `AccentDyed`. The first dye on a side **replaces** the default starting color; subsequent dyes blend with the existing color. This matches how `DyeableLeatherItem.dyeArmor` behaves in vanilla.

| Field | Default Color |
|-------|---------------|
| Main | `#FFFFFF` (white) |
| Accent | `#FFC3FA` (soft pink) |

> **Heads-up:** these defaults apply when you craft a piece directly (via the Create Sequenced Assembly). They do **not** apply to pieces equipped by an undyed Orange Sauce / Arrow / Pill — those roll one of the **16 vanilla dye colors** at random for the main color, with an auto-contrasting black/white accent. See [[Pills-and-Throwables#throwable-orange-sauce|Throwable Orange Sauce]].

## NBT Layout — Wearables

Tag root: `MelatowoinEquip`.

| Key | Type | Meaning |
|-----|------|---------|
| `MainColor` | int | Packed RGB |
| `AccentColor` | int | Packed RGB |
| `MainDyed` | bool | Has the main color been explicitly dyed? |
| `AccentDyed` | bool | Has the accent color been explicitly dyed? |

## NBT Layout — Orange Pill / Sauce / Arrow

The Orange Pill stores **four pairs** of colors (one per cat piece) so colors persist through the pill → sauce → arrow → equipped chain.

Tag root: `MelatoColors`.

| Key | Meaning |
|-----|---------|
| `EM`, `EA` | Ear main, ear accent |
| `TM`, `TA` | Tail main, tail accent |
| `PM`, `PA` | Paws main, paws accent |
| `BM`, `BA` | Toe-beans (boots) main, accent |

In the Orange Pill's own dye recipe, the same two-color mixing applies — main/accent are both written into all four piece slots. (You can't dye the pill's pieces independently from each other via the pill itself; for per-piece colors, dye each wearable separately.)

## Advanced Tooltip

With F3+H on, dyeable pieces show `Main: #RRGGBB  Accent: #RRGGBB`.

## Two-Pass Rendering

Each piece uses two grayscale entity textures (`*_layer1.png` and `*_layer2.png`) — layer 1 covers the main-color regions (accent is transparent), layer 2 covers the accent regions. The client renderer tints each layer with the corresponding NBT color.

See `DyeableEquipmentItem.getEntityLayer1()` / `getEntityLayer2()` in the source.
