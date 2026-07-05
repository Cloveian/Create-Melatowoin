package net.melatowoin.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-authoritative store of every connected player's render preferences.
 * Updated whenever a client sends a config-update packet; queried when a new
 * player joins so the server can mirror everyone's current preferences down
 * to that player.
 */
public final class WearerConfigStore {

    private static final Map<UUID, WearerRenderConfig> CONFIGS = new ConcurrentHashMap<>();

    private WearerConfigStore() {}

    public static void put(UUID uuid, WearerRenderConfig config) {
        CONFIGS.put(uuid, config);
    }

    public static void remove(UUID uuid) {
        CONFIGS.remove(uuid);
    }

    public static WearerRenderConfig get(UUID uuid) {
        return CONFIGS.getOrDefault(uuid, WearerRenderConfig.DEFAULT);
    }

    public static Map<UUID, WearerRenderConfig> all() {
        return CONFIGS;
    }
}
