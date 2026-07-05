package net.melatowoin.client;

import net.melatowoin.network.SoundSourceHintPacket;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Short-lived cache of server-sent sound source hints. The volume mixin
 * consults this when processing a {@code SoundInstance} to learn — without
 * heuristics or local lookups — who actually caused the sound and what they
 * had equipped at that moment, both computed server-side.
 *
 * Hints expire after {@link #HINT_TTL_MS} (250 ms by default) which is
 * generous enough to cover network latency but short enough that a stale
 * hint can't match a later unrelated sound at the same position.
 */
public final class ClientSoundHints {

    private static final int  MAX_HINTS    = 32;
    private static final long HINT_TTL_MS  = 250L;
    private static final double POS_EPS_SQ = 0.25; // 0.5 block tolerance

    public record Hint(int sourceEntityId, byte equipmentFlags,
                       double x, double y, double z,
                       ResourceLocation soundId, long expireMs) {
        public boolean hasFlag(int flag) {
            return (equipmentFlags & flag) != 0;
        }
    }

    private static final Deque<Hint> hints = new ArrayDeque<>();

    private ClientSoundHints() {}

    public static void handle(SoundSourceHintPacket pkt) {
        long expire = System.currentTimeMillis() + HINT_TTL_MS;
        synchronized (hints) {
            hints.addFirst(new Hint(pkt.sourceEntityId, pkt.equipmentFlags,
                    pkt.x, pkt.y, pkt.z, pkt.soundId, expire));
            while (hints.size() > MAX_HINTS) hints.removeLast();
        }
    }

    /**
     * Returns the most recent live hint matching the given (position, sound),
     * or {@code null} if no hint matches.
     */
    public static Hint lookup(double x, double y, double z, ResourceLocation soundId) {
        if (soundId == null) return null;
        long now = System.currentTimeMillis();
        synchronized (hints) {
            Iterator<Hint> it = hints.iterator();
            while (it.hasNext()) {
                Hint h = it.next();
                if (h.expireMs < now) {
                    it.remove();
                    continue;
                }
                if (!h.soundId.equals(soundId)) continue;
                double dx = h.x - x;
                double dy = h.y - y;
                double dz = h.z - z;
                if (dx * dx + dy * dy + dz * dz <= POS_EPS_SQ) {
                    return h;
                }
            }
        }
        return null;
    }
}
