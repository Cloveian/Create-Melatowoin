package net.melatowoin.client;

/**
 * Three-state marker that tells the volume mixin who emitted the sound about
 * to be processed:
 *
 *   LOCAL     — we are inside an emit path known to be the local player
 *               (their own {@code Entity.playSound}, or a server packet whose
 *               entity id matches their entity id). Apply muffling.
 *   NOT_LOCAL — we are inside an emit path for a known *other* entity (a
 *               server packet whose entity id is some mob, another player,
 *               etc.). Never muffle.
 *   NONE      — no information; fall back to the distance heuristic and
 *               whatever entity-proximity check the caller wants to apply.
 *
 * Two separate counters tolerate the (rare) case where one emit recurses into
 * another. Sound processing in Minecraft is synchronous on the render thread,
 * so plain ints are safe.
 */
public final class LocalPlayerSoundMarker {

    private static int localDepth    = 0;
    private static int notLocalDepth = 0;

    private LocalPlayerSoundMarker() {}

    public static void enterLocal()    { localDepth++; }
    public static void exitLocal()     { if (localDepth    > 0) localDepth--;    }
    public static void enterNotLocal() { notLocalDepth++; }
    public static void exitNotLocal()  { if (notLocalDepth > 0) notLocalDepth--; }

    public static boolean isLocal()    { return localDepth    > 0; }
    public static boolean isNotLocal() { return notLocalDepth > 0; }
}
