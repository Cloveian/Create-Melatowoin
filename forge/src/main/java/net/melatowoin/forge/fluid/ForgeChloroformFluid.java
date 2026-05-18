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

public class ForgeChloroformFluid {

    static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MelatowoinMod.MOD_ID);

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register("chloroform",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.melatowoin.chloroform")
                    .canSwim(true)
                    .canDrown(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register() {
        var modBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        FLUID_TYPES.register(modBus);

        ModFluids.CHLOROFORM = ModFluids.FLUIDS.register("chloroform",
                () -> new ForgeFlowingFluid.Source(makeProps()));
        ModFluids.CHLOROFORM_FLOWING = ModFluids.FLUIDS.register("chloroform_flowing",
                () -> new ForgeFlowingFluid.Flowing(makeProps()));

        ModItems.CHLOROFORM_BUCKET = ModItems.ITEMS.register("chloroform_bucket",
                () -> new net.minecraft.world.item.BucketItem(
                        ModFluids.CHLOROFORM.get(),
                        new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    }

    private static ForgeFlowingFluid.Properties makeProps() {
        return new ForgeFlowingFluid.Properties(
                TYPE,
                () -> (ForgeFlowingFluid.Source) ModFluids.CHLOROFORM.get(),
                () -> (ForgeFlowingFluid.Flowing) ModFluids.CHLOROFORM_FLOWING.get())
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1);
    }
}
