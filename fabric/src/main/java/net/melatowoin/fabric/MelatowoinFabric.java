package net.melatowoin.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.entity.OrangeProjectileEntity;
import net.melatowoin.fabric.event.FabricEventHandlers;
import net.melatowoin.fabric.fluid.FabricFluids;

public class MelatowoinFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register platform fluids before common init so bucket items can reference them
        FabricFluids.register();

        MelatowoinMod.init();

        // Register server-side event handlers
        FabricEventHandlers.register();

        // If Accessories is installed, curse hat and belt slots on orange sauce hit
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            OrangeProjectileEntity.onHitExtra = AccessoriesSauceHelper::equipEarsAndTail;
        }
    }
}
