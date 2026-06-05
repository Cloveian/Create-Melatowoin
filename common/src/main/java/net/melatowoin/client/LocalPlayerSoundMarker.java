package net.melatowoin.client;

/**
 * Tracks whether we're currently inside a call to {@code Entity.playSound}
 * dispatched by the local player. Set by {@code MixinEntityPlaySound} at HEAD
 * and cleared at RETURN; the sound-volume mixin reads it to know — without
 * relying on a distance heuristic — that a sound is definitely the local
 * player's.
 *
 * <p>Uses a static depth counter because Minecraft processes
 * {@code Entity.playSound → Level.playSound → SoundManager.play →
 * SoundInstance.getVolume()} synchronously on the render thread. The depth
 * tolerates the (rare) case where one playSound triggers another inside the
 * same frame.
 */
public final class LocalPlayerSoundMarker {
    private static int depth = 0;

    private LocalPlayerSoundMarker() {}

    public static void enter() { depth++; }

    public static void exit()  { if (depth > 0) depth--; }

    public static boolean active() { return depth > 0; }
}
