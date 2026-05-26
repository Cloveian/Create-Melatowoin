# Recipes — Create Machines

Every recipe that runs through a Create machine. Fluid amounts are in mb (Create's internal unit). For the natural flow charts, see [[Chloroform-Chain]] and [[Furrycore-Chain]].

The images below show each recipe with its input(s) on the left, the machine in the middle, and the output(s) on the right.

## Materials

| Output | Machine | Inputs |
|--------|---------|--------|
| Plastic | Mixer | dried kelp + andesite alloy |
| String Mess | Mill | white carpet (100 ticks) |
| Dust | Mill | String Mess (300 ticks) |
| Fur | Mixer | Dust + Plastic |
| Fur (alt) | Deployer | Cat spawn egg + Brush *(data-pack form — in-world, just brush a cat)* |
| High Quality Fabric | **Sequenced Assembly** on white carpet, **10 loops**: Deploy Fur → Deploy Plastic → Press |

## Chemistry

| Output | Machine | Inputs |
|--------|---------|--------|
| Vinegar (16200 mb) | Mixer (heated) | Bone Meal + Apple + 20250 mb Water |
| Calcium Acetate | Mixer (heated) | Egg + 8100 mb Vinegar |
| Acetone (10125 mb) | Mixer (**superheated**) | Calcium Acetate |
| Chloroform (4050 mb) + Water (40500 mb) | Mixer | 16200 mb Bleach + 20250 mb Acetone |

<table>
<tr>
<td align="center"><b>Vinegar</b><br><sub>Bone Meal + Apple + Water → Vinegar</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/vinegar.png" width="320"></td>
<td align="center"><b>Calcium Acetate</b><br><sub>Egg + Vinegar → Calcium Acetate</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/calcium-acetate.png" width="320"></td>
</tr>
<tr>
<td align="center"><b>Acetone</b><br><sub>Calcium Acetate → Acetone (superheated)</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/acetone.png" width="320"></td>
<td align="center"><b>Chloroform</b><br><sub>Bleach + Acetone → Chloroform + Water</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/chloroform.png" width="320"></td>
</tr>
</table>

## Recycling — Furrycore Track

| Output | Machine | Inputs |
|--------|---------|--------|
| Furrycore (5% per item) | Crusher | any `#forge:furrystuff` (a cat piece) |
| Unpure Furrynes + Bleach (8100 mb) | Mixer (heated) | Furrycore + 20250 mb Water |
| Pure Furrynes + 16 Eggs | Mixer | Unpure Furrynes + 81000 mb Water |

<table>
<tr>
<td align="center"><b>Furrycore</b><br><sub>Any furry cat piece → Furrycore (5%)</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/furry-core.png" width="320"></td>
<td align="center"><b>Unpure Furrynes + Bleach</b><br><sub>Furrycore + Water → Unpure Furrynes + Bleach</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/unpure-furrynes+bleach.png" width="320"></td>
</tr>
<tr>
<td align="center" colspan="2"><b>Pure Furrynes</b><br><sub>Unpure Furrynes + Water → Pure Furrynes + 16× Egg</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/pure-furrynes.png" width="320"></td>
</tr>
</table>

## Wearable Cat Pieces

All four are **Sequenced Assemblies, 3 loops** of *Deploy Fur → Deploy Plastic → Press*.

| Output | Base Ingredient |
|--------|-----------------|
| Cat Ears | Headband |
| Tail | High Quality Fabric |
| Paws | Mittens |
| Toe Beans | Socks |

## Pills, Sauces

| Output | Machine | Inputs |
|--------|---------|--------|
| Cyan Pill | Compacting | 20250 mb Chloroform |
| Throwable Cyan Sauce | Compacting | 20250 mb Chloroform + Glass Bottle |
| Throwable Cyan Sauce (alt) | Mixer | Cyan Pill + 20250 mb Water |
| Orange Pill | Compacting | Cyan Pill + Pure Furrynes + Cat Ears + Tail + Paws + Toe Beans |
| Throwable Orange Sauce | Mixer | Orange Pill + 20250 mb Water |

<table>
<tr>
<td align="center"><b>Cyan Pill</b><br><sub>Chloroform → Cyan Pill (Compacting)</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/blue-pill.png" width="320"></td>
<td align="center"><b>Orange Pill</b><br><sub>Cyan Pill + Pure Furrynes + Cat Ears + Tail + Paws + Toe Beans → Orange Pill</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/orange-pill.png" width="320"></td>
</tr>
<tr>
<td align="center" colspan="2"><b>Throwable Orange Sauce</b><br><sub>Orange Pill + Water → Throwable Orange Sauce (mixer, no heating)</sub><br><img src="https://raw.githubusercontent.com/Cloveian/Create-Melatowoin/main/images/crafting/throwable-orange-sause.png" width="320"></td>
</tr>
</table>

Orange Arrow is crafting-table only — see [[Recipes-Crafting-Table#orange-arrow|Orange Arrow]].
