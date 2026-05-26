# Wearable Cat Pieces

The four cat accessories. All share the same armor material (`melatowoin:cat_ears`), are dyeable in two colors, and have a special slot mapping if [[Accessories-Integration|Accessories]] is installed.

## Pieces

| Item | Armor Slot | Accessories Slot |
|------|-----------|------------------|
| Cat Ears | Helmet | Hat |
| Paws | Chestplate | Gloves |
| Tail | Leggings | Belt |
| Toe Beans | Boots | Shoes |

## Armor Stats

The `melatowoin:cat_ears` armor material:

| Stat | Value |
|------|-------|
| Durability multiplier | 40 |
| Protection (feet/legs/chest/head) | 1 / 3 / 5 / 2 |
| Enchantment value | 9 |
| Toughness | 0 |
| Knockback resistance | 0 |
| Equip sound | leather armor |
| Repair material | Fur |

## Special Abilities

### Paws — Silent Chests

While wearing Paws, opening a chest (or any inventory container that fires the container-open game event) produces no sound. Implemented by `MixinChestSound` and `MixinContainerGameEvent` in each platform module.

### Toe Beans — Silent Steps + Powder Snow Walking

While wearing Toe Beans:

- **Silent footsteps** — `MixinStepSound` cancels step sound for the wearer.
- **Walk on powder snow** — `MixinPowderSnow` hooks `PowderSnowBlock.canEntityWalkOnPowderSnow` and lets Toe Beans wearers stay on top, like leather boots.

### Cat Ears, Tail

Cosmetic only.

## Dyeing

All four are dyeable in two colors. See [[Dyeing-System]].

## Defaults

| Field | Default |
|-------|---------|
| Main color | `#FFFFFF` (white) |
| Accent color | `#FFC3FA` (soft pink) |

## Full Set

A "full set" means all four cat pieces equipped (slot or Accessories). Used by the orange-equip system as a "you are already a furry" check, so re-hitting someone with orange sauce won't double-equip.
