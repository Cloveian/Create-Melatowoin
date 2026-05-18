package net.melatowoin.forge.fluid;

/**
 * Placeholder — fluid wiring is handled directly in each ForgeXxxFluid class
 * by registering into ModFluids.FLUIDS (Architectury DeferredRegister) and
 * keeping a separate Forge DeferredRegister only for FluidType.
 *
 * The RegistrySupplier returned by ModFluids.FLUIDS.register(...) is the
 * Architectury-native supplier and can be assigned directly to ModFluids fields.
 */
public final class ForgeFluidHelper {
    private ForgeFluidHelper() {}
}
