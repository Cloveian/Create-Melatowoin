# Furrycore Chain

The recycling pathway — turn cat pieces back into raw chemistry inputs. Also the only source of **Bleach** and **Pure Furrynes**, both of which feed the Orange Pill recipe.

## The Chain

```
Cat pieces (tag #forge:furrystuff)    ──crush──►        Furrycore (5% chance per item)
Furrycore + water                     ──heated mix──►   Unpure Furrynes + Bleach
Unpure Furrynes + water               ──mix──►          Pure Furrynes + 16 Eggs
```

## Step Details

### 1. Furrycore

- **Machine:** Crusher
- **In:** Any item with the `#forge:furrystuff` tag — Cat Ears, Tail, Paws, or Toe Beans
- **Out:** 1× Furrycore (5% chance per crush)
- **Processing time:** 200 ticks

![Furrycore — any furry cat piece → Furrycore (Crusher, 5%)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/furry-core.png)

Yes, this destroys your hard-earned cat pieces. The 5% chance is intentional.

### 2. Bleach + Unpure Furrynes

- **Machine:** Mixer (heated)
- **In:** 1× Furrycore + 20250 mb Water
- **Out:** 1× Unpure Furrynes + 8100 mb Bleach (fluid)

![Unpure Furrynes + Bleach — Furrycore + Water → Unpure Furrynes + Bleach (heated mixer)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/unpure-furrynes+bleach.png)

The bleach feeds the chloroform recipe. The unpure furrynes feeds the next step.

### 3. Pure Furrynes

- **Machine:** Mixer (no heating required)
- **In:** 1× Unpure Furrynes + 81000 mb Water
- **Out:** 1× Pure Furrynes + 16× Egg

![Pure Furrynes — Unpure Furrynes + Water → Pure Furrynes + 16 Eggs (mixer, no heating)](https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/pure-furrynes.png)

The egg byproduct is convenient — it loops back into the [[Chloroform-Chain]] for Calcium Acetate.

## Why You Care

| Output | Used By |
|--------|---------|
| **Bleach** | The chloroform reaction (`Bleach + Acetone → Chloroform`) |
| **Pure Furrynes** | The Orange Pill recipe |

So: if you want the Orange Pill, you need both halves of this chain.

## Tag Reference

The crushing recipe consumes items in the `#forge:furrystuff` tag. The mod ships this tag pre-populated with the four cat pieces — see [[Tags-and-Game-Rules#forgefurrystuff|the tag reference]] for adding your own.
