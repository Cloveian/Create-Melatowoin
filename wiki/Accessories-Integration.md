# Accessories Integration

> Optional dependency: [Accessories](https://modrinth.com/mod/accessories) by WispForest.

When Accessories is installed, each cat piece can be worn in a dedicated Accessories slot — so you can keep your real armor on **and** wear the full cat outfit.

## Slot Mapping

| Cat Piece | Accessories Slot |
|-----------|------------------|
| Cat Ears | Hat |
| Paws | Gloves |
| Tail | Belt |
| Toe Beans | Shoes |

A cat piece will equip into either the armor slot OR the Accessories slot. The render layer checks both locations when deciding whether to draw the piece.

## How It Works (Per Loader)

### Fabric
- `AccessoriesIntegration` registers the cat pieces with the Accessories API at client init time.
- `AccessoriesSauceHelper` provides the orange-equip flow with a hook for placing items into Accessories slots instead of armor slots when those slots are available.
- `MixinAccessoriesHolderImpl` patches the Accessories holder so the orange-equip path can target it.

### Forge
- `AccessoriesForgeHelper` and `AccessoriesForgeIntegration` provide the equivalent integration. The forge build links against a specific NeoForge beta of Accessories — see [[Architecture#dependencies|Architecture]] for the version.

## What Happens When Accessories Is Missing?

Everything still works. The mod degrades gracefully:

- Cat pieces equip in the regular armor slots.
- The orange-equip flow puts pieces into the armor slots.
- `AccessoriesSlotHelper.findPawsInAccessories` defaults to returning `ItemStack.EMPTY`, so the silent-chest detection silently skips the Accessories check.

## Versions

The supported Accessories versions are pinned in `gradle.properties`:

```
accessories_fabric_version = 1.0.0-beta.47+1.20.1
accessories_forge_modrinth_id = WUQZDiqP   # specific NeoForge beta.46 build
```

If you're running a different Accessories version, you may see classloader errors at client init.
