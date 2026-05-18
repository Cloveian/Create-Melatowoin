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
 * Registers the Bleach fluid on Forge.
 * Bleach is flammable and is ignited (replaced by fire) when placed near lava.
 */
public class ForgeBleachFluid {

    static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MelatowoinMod.MOD_ID);

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register("bleach",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.melatowoin.bleach")
                    .canSwim(true)
                    .canDrown(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register() {
        var modBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        FLUID_TYPES.register(modBus);

        ModFluids.BLEACH = ModFluids.FLUIDS.register("bleach",
                () -> new ForgeFlowingFluid.Source(makeProps()));
        ModFluids.BLEACH_FLOWING = ModFluids.FLUIDS.register("bleach_flowing",
                () -> new ForgeFlowingFluid.Flowing(makeProps()));

        ModItems.BLEACH_BUCKET = ModItems.ITEMS.register("bleach_bucket",
                () -> new net.minecraft.world.item.BucketItem(
                        ModFluids.BLEACH.get(),
                        new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    }

    private static ForgeFlowingFluid.Properties makeProps() {
        return new ForgeFlowingFluid.Properties(
                TYPE,
                () -> (ForgeFlowingFluid.Source) ModFluids.BLEACH.get(),
                () -> (ForgeFlowingFluid.Flowing) ModFluids.BLEACH_FLOWING.get())
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1);
    }
}
