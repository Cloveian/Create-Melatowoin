package net.melatowoin.client;

/**
 * Categories used by {@code MixinSoundInstanceMuffle} when deciding whether
 * to scale a player-sourced sound by the cat-set sound reduction. Lives in a
 * non-mixin package because Forge's strict mixin class loader rejects direct
 * references to inner classes of a mixin class.
 */
public enum SoundKind {
    WALKING,
    HAND,
    EXCLUDED,
    OTHER
}
