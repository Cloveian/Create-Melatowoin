package net.melatowoin.server;

/**
 * Three render preferences a player can set in the config screen that should
 * propagate to everyone else's clients (so they see the wearer's choices, not
 * their own).
 *
 *  - {@link #liftEars()}        — shift cat ears 1px up when a helmet is worn underneath
 *  - {@link #hideHelmet()}      — hide the vanilla helmet model when wearing cat ears
 *  - {@link #hideChestplate()}  — hide the vanilla chestplate model when wearing paws
 */
public record WearerRenderConfig(boolean liftEars, boolean hideHelmet, boolean hideChestplate) {

    /** What new connections / unknown players are assumed to want. */
    public static final WearerRenderConfig DEFAULT = new WearerRenderConfig(true, false, false);
}
