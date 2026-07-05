package net.melatowoin;

import net.melatowoin.advancements.ModCriteria;
import net.melatowoin.event.CommonEventHandlers;
import net.melatowoin.network.EepyScreenPacket;
import net.melatowoin.network.SoundSourceHintPacket;
import net.melatowoin.network.WearerConfigC2SPacket;
import net.melatowoin.network.WearerConfigS2CPacket;
import net.melatowoin.server.WearerConfigStore;
import net.melatowoin.registry.*;
import net.melatowoin.registry.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MelatowoinMod {
    public static final String MOD_ID = "melatowoin";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Order matters: effects and entity types referenced by items must be registered first
        ModEffects.register();
        ModEntityTypes.register();
        // Fluids must be registered before Blocks (LiquidBlock refs fluid) and
        // Items (BucketItem refs fluid). On Fabric, Architectury calls factories
        // immediately during register(), so ordering here is load-order.
        ModFluids.register();
        ModBlocks.register();
        ModItems.register();
        ModRecipes.register();
        ModCreativeTab.register();
        ModGameRules.register();
        EepyScreenPacket.register();
        SoundSourceHintPacket.register();
        WearerConfigC2SPacket.register();
        WearerConfigS2CPacket.register();
        ModCriteria.init();
        CommonEventHandlers.register();

        // When a player joins the server, push every known config down to them
        // so they see everyone correctly from the moment they spawn in. When
        // they leave, drop their entry to keep the store from growing.
        dev.architectury.event.events.common.PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            for (var entry : WearerConfigStore.all().entrySet()) {
                WearerConfigS2CPacket.sendToPlayer(serverPlayer,
                        new WearerConfigS2CPacket(entry.getKey(), entry.getValue()));
            }
        });
        dev.architectury.event.events.common.PlayerEvent.PLAYER_QUIT.register(serverPlayer -> {
            WearerConfigStore.remove(serverPlayer.getUUID());
        });
    }
}
