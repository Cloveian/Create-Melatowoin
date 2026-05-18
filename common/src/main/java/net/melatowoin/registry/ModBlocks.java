package net.melatowoin.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.BLOCK);

    // Set by the platform module before register() is called.
    public static RegistrySupplier<? extends Block> ACETONE_BLOCK;
    public static RegistrySupplier<? extends Block> BLEACH_BLOCK;
    public static RegistrySupplier<? extends Block> CHLOROFORM_BLOCK;
    public static RegistrySupplier<? extends Block> VINEGAR_BLOCK;

    public static void register() {
        BLOCKS.register();
    }
}
