package net.melatowoin.forge;

import dev.architectury.platform.forge.EventBuses;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.entity.OrangeProjectileEntity;
import net.melatowoin.forge.fluid.ForgeAcetoneFluid;
import net.melatowoin.item.OrangeEquipHelper;
import net.melatowoin.forge.fluid.ForgeBleachFluid;
import net.melatowoin.forge.fluid.ForgeChloroformFluid;
import net.melatowoin.forge.fluid.ForgeVinegarFluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MelatowoinMod.MOD_ID)
public class MelatowoinForge {
    public MelatowoinForge() {
        // Required for Architectury's DeferredRegister to find this mod's event bus
        EventBuses.registerModEventBus(MelatowoinMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Register platform fluids first so bucket items can reference fluid objects
        // (each registers its FluidType DeferredRegister on the mod bus)
        ForgeAcetoneFluid.register();
        ForgeBleachFluid.register();
        ForgeChloroformFluid.register();
        ForgeVinegarFluid.register();

        // Common init: registers items, effects, entities, creative tab, game rules,
        // network packets, and cross-platform events (via Architectury)
        MelatowoinMod.init();

        // Default: equip in armor slots; override with Accessories priority logic if present
        OrangeProjectileEntity.onHitExtra = OrangeEquipHelper::defaultEquip;
        if (ModList.get().isLoaded("accessories")) {
            OrangeProjectileEntity.onHitExtra = AccessoriesForgeHelper::equipEarsAndTail;
        }

        // @Mod.EventBusSubscriber classes (ForgeEventHandlers, MelatowoinForgeClient)
        // are discovered automatically by Forge via annotation scanning.
    }
}
