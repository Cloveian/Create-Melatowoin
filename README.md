# Create: MelatOwOin >w<

A chaos-themed Create addon for **Minecraft 1.20.1**, on **Fabric and Forge**.
~~Ported from~~ Inspired by [Melatonin(Create)](https://modrinth.com/mod/createmelatonin) by Alos2019. Made by Cloveian :3

Adds dyeable cat ears, tails, paws, and toe beans — plus a full Create chloroform production line, throwable sauces that turn your friends into furries, and pills that probably aren't legal.

<!-- 📷 TODO image: images/hero.png — staged shot of a player in a full dyed cat outfit standing in front of a working chloroform setup. This is the banner. -->
![hero shot](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/hero.png)

<!-- 📷 TODO image: images/color-sampler.png — a spread of 8–12 wildly different freeform two-color combinations on one piece (e.g. ears) to show that the dye system is continuous, not a 16-pick. -->
![two-color dye sampler](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/color-sampler.png)

The grid below shows the **16-color random palette** that gets rolled when an *undyed* Orange Sauce hits a target — one of the 16 vanilla dye colors with an auto-contrasting black/white accent:

![the 16 random palette](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/random-palette.png)

![](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/tail-swoosh.gif)


---

## Highlights

- **Four-piece wearable cat outfit** — Ears, Tail, Paws, Toe Beans — each dyeable in **two colors** (main + accent), each with a small armor benefit.
- **Full Create chemistry chain** — Vinegar → Calcium Acetate → Acetone + Bleach → Chloroform, all using Create's Mixer, Compactor, Sequenced Assembly, etc.
- **Cat brushing** — right-click a cat with a Brush (vanilla item) to get Fur. Also works through a Create Deployer.
- **Throwables and arrows** — Throwable Cyan Sauce, Throwable Orange Sauce, and an Orange Arrow you can fire from any bow.
- **Two custom potion effects** — *Eepy* (a soft full-screen blackout with full movement lockout) and *Change* (the "you've been hit with orange sauce" effect).
- **Accessories support (optional)** — wear the cat pieces in Accessories slots (Hat / Belt / Gloves / Shoes) instead of armor slots.

---

## Wearable Cat Pieces

<!-- 📷 TODO image: images/full-set.png — one player front+back in a complete dyed set (ears+tail+paws+toebeans). The "complete look" shot. -->
![the full outfit](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/full-set.png)

| Item | Armor Slot | Accessories Slot | Special |
|------|-----------|------------------|---------|
| Cat Ears | Helmet | Hat | — |
| Tail | Leggings | Belt | — |
| Paws | Chestplate | Gloves | Silently open chests |
| Toe Beans | Boots | Shoes | Silent footsteps, walk on powder snow |

All four pieces are **two-color dyeable** in a crafting table:

> Place the piece in the crafting grid. Dyes **left or right** mix into the **main** color (fur). Dyes **above or below** mix into the **accent** color (inner ear / paw pad). Mixing follows the vanilla leather-armor color formula.

<!-- 📷 TODO image: images/dye-demo.gif — open crafting table, place ears + red dye left + blue dye above, show the result preview updating. Sells the two-color system in one loop. -->
![dyeing in action](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/dye-demo.gif)

> **Tip:** Get Fur by right-clicking a Cat with a vanilla Brush — works with a Create Deployer too.

<!-- 📷 TODO image: images/brush-cat.gif — short loop: player right-clicks cat → fur drops. Bonus points if you append a Create Deployer doing it on a loop. -->
![brushing a cat for fur](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/brush-cat.gif)

---

## Throwables, Pills, and Sauces

| Item | What it does |
|------|--------------|
| Cyan Pill | Eat it → *Eepy* effect (full-screen blackout, 10s) |
| Orange Pill | Eat it → instantly equips a full furry outfit on you (dyeable!) |
| Throwable Cyan Sauce | Glass bottle of chloroform — pure flavor item / decorative |
| Throwable Orange Sauce | Throw at a friend → equips them with a Curse-of-Binding-locked furry outfit + *Change* effect |
| Orange Arrow | Fires from any bow. Same effect as Orange Sauce, with range |

<!-- 📷 TODO image: images/orange-sauce-demo.gif — THE headline demo. Throw orange sauce at another player/villager → they get the full outfit + Change effect HUD icon. Aim for ≤6s, ≤10MB. -->
![orange sauce hit](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/orange-sauce-demo.gif)

<!-- 📷 TODO image: images/orange-arrow.gif — fire orange arrow from a bow at a mob, mob equips outfit on hit. Sells the "at range" angle. -->
![orange arrow](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/orange-arrow.gif)

The Orange Pill itself can be **dyed** the same way as the wearables — those colors carry through into the equipment it spawns.

---

## The Eepy Effect *zzz...*

A custom potion effect that opens a fullscreen black overlay client-side and clamps your movement speed to zero. There is no escape until it wears off.

<!-- 📷 TODO image: images/eepy-activate.gif — eat the Cyan Pill, watch the screen fade to black + movement lock in. Better than the static darkness shot for showing what Eepy actually does. Keep darkness.webp as a fallback. -->
![eepy effect activating](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/eepy-activate.gif)

![](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/darkness.webp)

---

## Getting Started

<!-- 📷 TODO image: images/chloroform-line.png — annotated photo of a working chloroform Create setup (vinegar mixer → CA mixer → superheated acetone → bleach+acetone mixer). Replaces a thousand words. -->
![chloroform production line](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/chloroform-line.png)

| Goal | How |
|------|-----|
| Fur | Right-click a cat with a Brush (vanilla item) — or use a Create Deployer holding a Brush on a Cat |
| Plastic | Mix dried kelp + andesite alloy |
| High Quality Fabric | Sequenced assembly on white carpet (fur, plastic, press) |
| Socks / Mittens / Headband | Crafting table from fabric |
| Cat Ears / Tail / Paws / Toe Beans | Sequenced assembly on each base item |
| Chloroform line | See [WIKI.md](WIKI.md) for the chemistry chart |
| Anything else | Check JEI/EMI/REI — every recipe shows there |

For the full recipe tree, item list, and effect details, see **[WIKI.md](WIKI.md)**.

---

## Dependencies

**Required**
- [Create](https://modrinth.com/mod/create-fabric) 6.0.8+ (Fabric) / [Create](https://modrinth.com/mod/create) 6.0.8+ (Forge)
- Architectury API
- Fabric API (Fabric only)

**Optional**
- [Accessories](https://modrinth.com/mod/accessories) — lets you wear the cat pieces in dedicated accessory slots instead of armor slots, so you can keep your real armor on.

---

## Building from Source

```bash
./gradlew build
```

Outputs land in `fabric/build/libs/` and `forge/build/libs/`.

The project is a standard Architectury multi-loader setup:

```
common/   — shared mod code (registries, items, effects, recipes, mixins)
fabric/   — Fabric-specific loader code, fluids, renderers
forge/    — Forge-specific loader code, fluids, renderers
```

---

## Game Rules

| Rule | Default | Purpose |
|------|---------|---------|
| `docat` | `true` | When `false`, brushing a cat will not yield Fur. |

---

## License

[Apache-2.0](LICENSE).

- **Original Melatonin(Create)** by [Alos2019](https://modrinth.com/user/Alos2019)
- **Create: MelatOwOin** by Cloveian :3

---

<!--
==============================================================================
IMAGE TODO INDEX — every placeholder image referenced above, in order.
Grep for "📷 TODO image" to jump between them.
Drop the file into images/ with the exact filename and the placeholder
auto-renders. Delete the slot if you don't want it.
==============================================================================
  images/hero.png             — full-outfit player + chloroform setup banner
  images/color-sampler.png    — spread of varied user-dyed 2-color combos
  images/full-set.png         — front+back of complete dyed outfit
  images/dye-demo.gif         — two-color dye in the crafting table
  images/brush-cat.gif        — brushing a cat (manual + Deployer optional)
  images/orange-sauce-demo.gif — throw orange sauce → friend becomes a furry
  images/orange-arrow.gif     — orange arrow shot from a bow
  images/eepy-activate.gif    — Cyan Pill → black overlay activating
  images/chloroform-line.png  — annotated Create chemistry build
==============================================================================
-->
