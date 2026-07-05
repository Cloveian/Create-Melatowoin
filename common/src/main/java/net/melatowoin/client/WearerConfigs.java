package net.melatowoin.client;

import net.melatowoin.MelatowoinConfig;
import net.melatowoin.server.WearerRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side cache of every known player's render preferences, populated
 * from server-broadcast {@code WearerConfigS2CPacket} messages.
 *
 * <p>The renderers call {@link #get(LivingEntity)} when deciding how to draw
 * a player; this returns the live local config for "self", or the cached
 * value for any other player, falling back to the default until the server's
 * broadcast arrives.
 */
public final class WearerConfigs {

    private static final Map<UUID, WearerRenderConfig> CACHE = new ConcurrentHashMap<>();

    private WearerConfigs() {}

    public static void put(UUID uuid, WearerRenderConfig config) {
        CACHE.put(uuid, config);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static WearerRenderConfig get(LivingEntity entity) {
        if (!(entity instanceof Player player)) return WearerRenderConfig.DEFAULT;
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.player != null && mc.player.getUUID().equals(player.getUUID())) {
            // Local player: always read live from the saved config so changes
            // take effect immediately without a network round-trip.
            return new WearerRenderConfig(
                    MelatowoinConfig.getEarsLiftUnderHelmet(),
                    MelatowoinConfig.getHideHelmetWithCatEars(),
                    MelatowoinConfig.getHideChestplateWithPaws());
        }
        return CACHE.getOrDefault(player.getUUID(), WearerRenderConfig.DEFAULT);
    }
}
