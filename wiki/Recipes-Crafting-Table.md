# Recipes — Crafting Table

Vanilla 3×3 recipes. For Create-machine recipes, see [[Recipes-Create-Machines]].

## Socks

```
X . X
X . X
. . .
```

X = `melatowoin:fabric` → 1× Socks.

## Mittens

```
X . X
. . .
. . .
```

X = `melatowoin:fabric` → 1× Mittens.

## Headband

```
N N N
N X N
. . .
```

N = `minecraft:iron_nugget`, X = `melatowoin:fabric` → 1× Headband.

## Dyeable Equipment Dye

Custom recipe — place a cat piece in the grid, surround with dyes. See [[Dyeing-System]].

Serializer ID: `melatowoin:dyeable_equipment_dye`.

Example: Cat Ears + Blue Dye (above) + Orange Dye (left) → Cat Ears with orange main + blue accent.

![Dyeable Equipment Dye — Cat Ears + Blue Dye + Orange Dye → dyed Cat Ears](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/dye-crafting.png)

## Orange Pill Dye

Custom recipe — place an Orange Pill in the grid, surround with dyes. Same mechanic as above but on Orange Pill NBT.

Serializer ID: `melatowoin:orange_pill_dye`.

## Orange Arrow

Shapeless:

- 1× `melatowoin:orangeable` (Throwable Orange Sauce)
- 1× `minecraft:arrow`

→ 1× Orange Arrow.

Serializer ID: `melatowoin:orange_arrow` — a wrapped shapeless recipe that copies color NBT from the sauce into the arrow.

![Orange Arrow — Throwable Orange Sauce + Arrow → Orange Arrow](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/orange-arrow.png)
