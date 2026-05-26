# Architecture

> Contributor / packmaker reference. Skip if you're just playing.

## Project Layout

```
common/      Loader-independent code (registries, items, effects, recipes, mixins, client models)
  src/main/java/net/melatowoin/...
fabric/      Fabric-specific entry point, fluids, renderers, accessory hooks
forge/       Forge-specific entry point, fluids, renderers, accessory hooks
```

Built with **Architectury 9.2.14** for loader abstraction. Registration is `dev.architectury.registry.registries.DeferredRegister`.

## Dependencies

Pinned in `gradle.properties`:

| Dep | Version |
|-----|---------|
| Minecraft | 1.20.1 |
| Architectury | 9.2.14 |
| Fabric Loader | 0.15.11 |
| Fabric API | 0.88.1+1.20.1 |
| Forge | 1.20.1-47.2.20 |
| Create (Fabric) | 6.0.8.1+build.1744-mc1.20.1 |
| Create (Forge) | 6.0.8-291 |
| Accessories (Fabric) | 1.0.0-beta.47+1.20.1 |
| Accessories (Forge) | Modrinth ID `WUQZDiqP` (NeoForge beta.46) |

## Build

```bash
./gradlew build
```

Outputs:

```
fabric/build/libs/melatowoin-<version>.jar
forge/build/libs/melatowoin-<version>.jar
```

## Registration Order

In `net.melatowoin.MelatowoinMod.init`:

```
Effects → EntityTypes → Fluids → Blocks → Items → Recipes → CreativeTab → GameRules → EepyScreenPacket → CommonEventHandlers
```

Order matters because:
- `BucketItem` references the fluid → fluids before items.
- `LiquidBlock` references the fluid → fluids before blocks.
- `OrangeArrowItem` references the `ORANGE_ARROW` entity type → entity types before items.
- The Architectury Fabric registry path invokes the factory immediately at `register()`, so any forward reference will NPE.

## Platform-Only Registry Holes

`ModFluids`, `ModBlocks`, and the four bucket entries in `ModItems` are declared as **uninitialised static fields** in `common/`. The Fabric and Forge modules populate them before calling `register()`. This is because each loader has its own fluid API (Forge `FluidType` + still/flowing pair, Fabric attributes via `SimpleFluid`/Architectury) — implementation has to live per-loader.

## Mixins

### Common (`melatowoin.mixins.json`)

| Mixin | Target | Purpose |
|-------|--------|---------|
| `MixinCatInteract` | `Cat.mobInteract` | Hooks cat-brushing so it works for both Player and Create Deployer interactions. Calls `CatBrushHandler.handle`. |
| `MixinPowderSnow` | `PowderSnowBlock.canEntityWalkOnPowderSnow` | Allows Toe Beans wearers to walk on powder snow. |
| `MixinItemInHandRenderer` | client `ItemInHandRenderer` | Small rendering tweak for held items. |

### Per-Loader

| Mixin | Loader | Target | Purpose |
|-------|--------|--------|---------|
| `MixinStepSound` | both | `LivingEntity` step sound | Silences Toe Beans wearer's footsteps. |
| `MixinChestSound` | both | Chest open/close sound | Silences chest-opening when wearing Paws. |
| `MixinContainerGameEvent` | both | container game events | Silences container-related game events for Paws wearer. |
| `MixinLivingEntityEffects` | Fabric | `LivingEntity` effect add/remove | Bridges effect lifecycle into `EepyEffect.onStarted`/`onExpired`. |
| `MixinAccessoriesHolderImpl` | Fabric | Accessories internals | Lets the orange-equip flow target Accessories slots. |
| `MixinEnchantment` | Fabric | Enchantment compat | Various enchantment compatibility fixes for the cat pieces. |

The Forge equivalent of `MixinLivingEntityEffects` is event-based instead of mixin-based — see `forge/.../event/ForgeEventHandlers.java`.

## Custom Recipe Serializers

Registered in `ModRecipes`:

| ID | Class | Purpose |
|----|-------|---------|
| `melatowoin:dyeable_equipment_dye` | `DyeableEquipmentDyeRecipe` | Two-color crafting-table dye recipe for cat pieces. |
| `melatowoin:orange_pill_dye` | `OrangePillDyeRecipe` | Two-color crafting-table dye recipe for Orange Pill. |
| `melatowoin:orange_pill_to_sauce` | `OrangePillToSauceRecipe` | Wraps a vanilla shapeless recipe to copy color NBT. |
| `melatowoin:orange_arrow` | `OrangeArrowRecipe` | Wraps a vanilla shapeless recipe to copy color NBT into the arrow. |

## Networking

One Architectury packet: `melatowoin:eepy_screen`, with two payloads (open / close). Used only by the Eepy effect. Registered in `EepyScreenPacket.register()`.

## Cross-Loader Hooks

A handful of `BiConsumer` / `Function` fields in common code are set by the platform module at init time:

| Hook | Set by | Purpose |
|------|--------|---------|
| `OrangeProjectileEntity.onHitExtra` | both | Equip orange outfit + apply Change. Different per-loader because of Accessories slot handling. |
| `AccessoriesSlotHelper.findPawsInAccessories` | both | Find a Paws item in Accessories slots (for the silent-chest detector). |

## Useful Constants

```
MOD_ID                        = "melatowoin"
EEPY_SPEED_UUID               = a9b8c7d6-e5f4-3210-a9b8-c7d6e5f43210
DEFAULT_MAIN_COLOR            = 0xFFFFFF
DEFAULT_ACCENT_COLOR          = 0xFFC3FA

Armor material: "melatowoin:cat_ears"
  durability multiplier       = 40
  protection (feet/legs/chest/head) = {1, 3, 5, 2}
  enchantment value           = 9
  toughness / knockback res   = 0 / 0
```

## File-System Reference

| Path | What lives there |
|------|------------------|
| `common/src/main/java/net/melatowoin/MelatowoinMod.java` | Mod init, registration order |
| `common/src/main/java/net/melatowoin/registry/` | All `DeferredRegister` setups |
| `common/src/main/java/net/melatowoin/item/` | Item classes (Cat pieces, pills, sauces, arrow item) |
| `common/src/main/java/net/melatowoin/entity/` | Throwable + arrow projectiles |
| `common/src/main/java/net/melatowoin/effect/` | Eepy + Change effects |
| `common/src/main/java/net/melatowoin/event/` | Brush handling |
| `common/src/main/java/net/melatowoin/recipe/` | Custom recipes |
| `common/src/main/java/net/melatowoin/mixin/` | Shared mixins |
| `common/src/main/java/net/melatowoin/network/` | Eepy screen packet |
| `common/src/main/java/net/melatowoin/client/` | Common client code (models, color renderer, EepyScreen) |
| `common/src/main/resources/assets/melatowoin/` | Lang, icons, textures |
| `common/src/main/resources/data/melatowoin/recipes/` | All recipe JSONs |
| `common/src/main/resources/melatowoin.mixins.json` | Mixin config |
| `common/src/main/resources/melatowoin.accesswidener` | Access widener |
| `fabric/`, `forge/` | Platform-specific code |
