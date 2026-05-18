package net.melatowoin.forge;

import net.melatowoin.MelatowoinMod;
import net.melatowoin.forge.fluid.ForgeAcetoneFluid;
import net.melatowoin.forge.fluid.ForgeBleachFluid;
import net.melatowoin.forge.fluid.ForgeChloroformFluid;
import net.melatowoin.forge.fluid.ForgeVinegarFluid;
import net.minecraftforge.fml.common.Mod;

@Mod(MelatowoinMod.MOD_ID)
public class MelatowoinForge {
    public MelatowoinForge() {
        // Register platform fluids first so bucket items can reference fluid objects
        // (each registers its FluidType DeferredRegister on the mod bus)
        ForgeAcetoneFluid.register();
        ForgeBleachFluid.register();
        ForgeChloroformFluid.register();
        ForgeVinegarFluid.register();

        // Common init: registers items, effects, entities, creative tab, game rules,
        // network packets, and cross-platform events (via Architectury)
        MelatowoinMod.init();

        // @Mod.EventBusSubscriber classes (ForgeEventHandlers, MelatowoinForgeClient)
        // are discovered automatically by Forge via annotation scanning.
    }
}
