# Tags and Game Rules

## Item Tags

### `#forge:furrystuff`

Default contents: `melatowoin:cat_ears`, `melatowoin:tail`, `melatowoin:paws`, `melatowoin:toe_beans`.

Defined at `data/melatowoin/tags/items/furrystuff.json` (the tag's mod-namespace path; the recipe references `forge:furrystuff` to make use of Forge's common-namespace conventions).

**Used by:** the Furrycore crushing recipe. See [[Furrycore-Chain]].

To extend the tag (e.g. for pack-makers who want other items to count as "furry stuff"), drop a tag JSON in your data pack at:

```
data/forge/tags/items/furrystuff.json
```

with `replace: false` and your additions.

## Game Rules

### `docat`

| Property | Value |
|----------|-------|
| Type | Boolean |
| Default | `true` |
| Category | Player |
| Translation key | `gamerule.docat` |

**Effect:** When `false`, brushing a cat does **not** drop Fur. The Brush still loses durability via vanilla behavior, but no fur is added. Useful for adventure maps where you don't want players grinding cats for fur.

Set it with:

```
/gamerule docat false
```
