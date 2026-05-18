package net.melatowoin;

import net.melatowoin.event.CommonEventHandlers;
import net.melatowoin.network.EepyScreenPacket;
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
        CommonEventHandlers.register();
    }
}
