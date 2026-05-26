package net.melatowoin.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.entity.OrangeProjectileEntity;
import net.melatowoin.fabric.event.FabricEventHandlers;
import net.melatowoin.fabric.fluid.FabricFluids;
import net.melatowoin.item.OrangeEquipHelper;

public class MelatowoinFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register platform fluids before common init so bucket items can reference them
        FabricFluids.register();

        MelatowoinMod.init();

        // Register server-side event handlers
        FabricEventHandlers.register();

        // Default: equip in armor slots; override with Accessories priority logic if present
        OrangeProjectileEntity.onHitExtra = OrangeEquipHelper::defaultEquip;
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            OrangeProjectileEntity.onHitExtra = (entity, stack) -> AccessoriesSauceHelper.equipEarsAndTail(entity, stack);
        }
    }
}
