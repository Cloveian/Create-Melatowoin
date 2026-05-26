# Status Effects

Two custom potion effects.

## Eepy — `melatowoin:eepy`

> *zzz...*

| Property | Value |
|----------|-------|
| Category | HARMFUL |
| Color | `#FF000C` |
| Built-in attribute | Movement Speed −255 (Operation: ADDITION) |

### Behavior

- **Server → client open packet** — When the effect is first applied to a Player, the server sends an `EepyScreenPacket.Open` to that player.
- **Client overlay** — The client opens an `EepyScreen` (a black fullscreen screen) titled `screen.melatowoin.eepy` ("zzz..."). The player cannot interact with the world.
- **Movement clamp** — The built-in attribute modifier brings movement speed to zero (negative-clamped).
- **Server → client close packet** — When the effect expires or is removed, the server sends `EepyScreenPacket.Close` and the client closes the screen.

### Bridges

The platform mixin `MixinLivingEntityEffects` (Fabric) and the equivalent Forge event listener hook the effect-add and effect-remove lifecycle and route them into `EepyEffect.onStarted` / `EepyEffect.onExpired`.

### How to get it

Eat a [[Pills-and-Throwables#cyan-pill|Cyan Pill]] → 200 ticks (10s) of Eepy.

## Change — `melatowoin:change`

> The "you've been hit with orange sauce" effect.

| Property | Value |
|----------|-------|
| Category | NEUTRAL |
| Color | `#FFB000` |
| Default duration | 4000 ticks (≈3:20) when applied by orange sauce/arrow |

### Behavior

Does nothing on its own. The actual cat-outfit equipping happens in `OrangeProjectileEntity.onHitExtra` (and `OrangePillItem.finishUsingItem` for the pill) **before** Change is applied.

Treat Change as the visible "you've been hit" indicator. The effect's color and HUD icon make it obvious that the target was orange-saucer'd, since the equipped pieces are otherwise indistinguishable from voluntarily-worn ones.

### How to get it

- Get hit by a Throwable Orange Sauce, or
- Get hit by an Orange Arrow, or
- Eat an Orange Pill (self-inflict).
