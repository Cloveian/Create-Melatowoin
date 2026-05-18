package net.melatowoin.forge.fluid;

import net.melatowoin.MelatowoinMod;
import net.melatowoin.registry.ModFluids;
import net.melatowoin.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers the Acetone fluid on Forge.
 *
 * Fluids themselves are registered via ModFluids.FLUIDS (Architectury DeferredRegister),
 * so the RegistrySupplier references in ModFluids are native Architectury suppliers.
 * FluidType uses Forge's own DeferredRegister since Architectury has no registry for it.
 */
public class ForgeAcetoneFluid {

    // Forge-only: FluidType registry
    static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MelatowoinMod.MOD_ID);

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register("acetone",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.melatonin.acetone")
                    .canSwim(true)
                    .canDrown(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register() {
        var modBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        FLUID_TYPES.register(modBus);

        // Register source and flowing into Architectury's DeferredRegister
        // so ModFluids gets proper RegistrySupplier references.
        ModFluids.ACETONE = ModFluids.FLUIDS.register("acetone",
                () -> new ForgeFlowingFluid.Source(makeProps()));
        ModFluids.ACETONE_FLOWING = ModFluids.FLUIDS.register("acetone_flowing",
                () -> new ForgeFlowingFluid.Flowing(makeProps()));

        // Register bucket item via common ITEMS DeferredRegister
        ModItems.ACETONE_BUCKET = ModItems.ITEMS.register("acetone_bucket",
                () -> new net.minecraft.world.item.BucketItem(
                        ModFluids.ACETONE.get(),
                        new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    }

    private static ForgeFlowingFluid.Properties makeProps() {
        // Note: ForgeFlowingFluid.Properties takes Supplier<FluidType> and Supplier<ForgeFlowingFluid.Source/Flowing>
        // We use lambda references back to ModFluids so they resolve lazily.
        return new ForgeFlowingFluid.Properties(
                TYPE,
                () -> (ForgeFlowingFluid.Source) ModFluids.ACETONE.get(),
                () -> (ForgeFlowingFluid.Flowing) ModFluids.ACETONE_FLOWING.get())
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1);
    }
}
