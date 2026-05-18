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

public class ForgeVinegarFluid {

    static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MelatowoinMod.MOD_ID);

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register("vinegar",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.melatonin.vinegar")
                    .canSwim(true)
                    .canDrown(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register() {
        var modBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        FLUID_TYPES.register(modBus);

        ModFluids.VINEGAR = ModFluids.FLUIDS.register("vinegar",
                () -> new ForgeFlowingFluid.Source(makeProps()));
        ModFluids.VINEGAR_FLOWING = ModFluids.FLUIDS.register("vinegar_flowing",
                () -> new ForgeFlowingFluid.Flowing(makeProps()));

        ModItems.VINEGAR_BUCKET = ModItems.ITEMS.register("vinegar_bucket",
                () -> new net.minecraft.world.item.BucketItem(
                        ModFluids.VINEGAR.get(),
                        new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    }

    private static ForgeFlowingFluid.Properties makeProps() {
        return new ForgeFlowingFluid.Properties(
                TYPE,
                () -> (ForgeFlowingFluid.Source) ModFluids.VINEGAR.get(),
                () -> (ForgeFlowingFluid.Flowing) ModFluids.VINEGAR_FLOWING.get())
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1);
    }
}
