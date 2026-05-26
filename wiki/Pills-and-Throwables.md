# Pills and Throwables

The "chaos line" of the mod — five items built on the chloroform chemistry. See [[Chloroform-Chain]] for how to make them.

## Cyan Pill

- **Action:** Eat (always-eat food, 0 nutrition).
- **Effect:** Applies *Eepy* for 10 seconds. See [[Status-Effects]].
- **Recipe:** Compacting (Mechanical Press over a Basin) — 20250 mb Chloroform → 1× Cyan Pill.

![Cyan Pill — Chloroform (in Basin) + Mechanical Press → Cyan Pill](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/blue-pill.png)

## Orange Pill

- **Action:** Eat (always-eat food, 0 nutrition).
- **Effect:** Immediately equips you with a full dyeable cat outfit using the pill's stored colors, then applies *Change*.
- **Dyeable:** yes — see [[Dyeing-System]].
- **Recipe:** Compacting (Mechanical Press over a Basin) — 1× Cyan Pill + 1× Pure Furrynes + 1× Cat Ears + 1× Tail + 1× Paws + 1× Toe Beans → 1× Orange Pill.

![Orange Pill — Cyan Pill + Pure Furrynes + Cat Ears + Tail + Paws + Toe Beans → Orange Pill (Compacting)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/orange-pill.png)

The Orange Pill stores four pairs of colors (one per piece). When you eat it (or hit someone with the sauce/arrow form), the equipped pieces inherit those colors.

## Throwable Cyan Sauce

- **Action:** Right-click to throw.
- **Projectile:** `melatowoin:cyan_projectile`.
- **On hit:** No on-hit behavior in the projectile class — it's the decorative "chloroform-in-a-bottle" variant.
- **Recipes:**
  - Compactor: 20250 mb Chloroform + Glass Bottle, or
  - Mixer: Cyan Pill + 20250 mb water

## Throwable Orange Sauce

- **Action:** Right-click to throw.
- **Projectile:** `melatowoin:orange_projectile`.
- **On hit (LivingEntity):**
  1. Skips if the target already has all four cat pieces equipped (full set).
  2. Skips if the target's armor slots aren't free.
  3. Otherwise: equips Cat Ears + Paws + Tail + Toe Beans into the armor slots, each enchanted with **Curse of Binding**.
  4. Applies the *Change* effect for 4000 ticks.
  5. Pieces inherit the sauce's stored colors (from the Orange Pill that made it). If the sauce was never dyed, the equipper rolls a **random color from the 16 vanilla `DyeColor` values** as the main color, and auto-picks a contrasting **black or white** as the accent (black if the main is light — white/gray/light-gray — white otherwise). All four pieces share the same rolled colors per hit.
- **Recipe:** Mixer (no heating required) — 1× Orange Pill + 20250 mb Water → 1× Throwable Orange Sauce.

![Throwable Orange Sauce — Orange Pill + Water → Throwable Orange Sauce (mixer, no heating)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/throwable-orange-sause.png)

> If the Accessories mod is present, the Fabric Accessories hook places pieces into the Accessories slots when those slots are available instead of the armor slots.

## Orange Arrow

- **Action:** Loaded into any bow as ammunition.
- **Entity:** `melatowoin:orange_arrow` (extends `AbstractArrow`).
- **On hit:** Same orange-equip behavior as Throwable Orange Sauce, using the colors stored on the source sauce stack (carried into the arrow at craft time).
- **Recipe:** Crafting table, shapeless — 1× Throwable Orange Sauce + 1× Arrow → 1× Orange Arrow. A custom `melatowoin:orange_arrow` serializer copies the color NBT from the sauce into the arrow.

![Orange Arrow — Throwable Orange Sauce + Arrow → Orange Arrow (shapeless crafting)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/orange-arrow.png)

## The Full Pipeline

```
Chloroform ──compact──► Cyan Pill ──┐
                                    ├──compact + Pure Furrynes + 4 cat pieces──► Orange Pill
                                    │                                              │
                              (also mix + water = Throwable Cyan Sauce)            │
                                                                                   │
                                                  Orange Pill + water ──mix──► Throwable Orange Sauce
                                                                                   │
                                                              + Arrow (crafting) ──► Orange Arrow
```
