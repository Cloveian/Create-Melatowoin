package net.melatowoin.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

/**
 * Common-side fluid registry stubs.
 * Actual Fluid implementations live in the platform-specific modules
 * (forge/fluid/ and fabric/fluid/) and register themselves here via
 * the RegistrySupplier references below.
 *
 * The platform modules call ModFluids.register() after populating
 * ACETONE, BLEACH, CHLOROFORM, and VINEGAR.
 */
public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.FLUID);

    // These suppliers are set by the platform module before register() is called.
    public static RegistrySupplier<? extends Fluid> ACETONE;
    public static RegistrySupplier<? extends Fluid> ACETONE_FLOWING;
    public static RegistrySupplier<? extends Fluid> BLEACH;
    public static RegistrySupplier<? extends Fluid> BLEACH_FLOWING;
    public static RegistrySupplier<? extends Fluid> CHLOROFORM;
    public static RegistrySupplier<? extends Fluid> CHLOROFORM_FLOWING;
    public static RegistrySupplier<? extends Fluid> VINEGAR;
    public static RegistrySupplier<? extends Fluid> VINEGAR_FLOWING;

    public static void register() {
        FLUIDS.register();
    }
}
