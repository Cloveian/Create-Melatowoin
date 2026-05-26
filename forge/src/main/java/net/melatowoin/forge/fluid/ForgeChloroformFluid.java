package net.melatowoin.forge.fluid;

import net.melatowoin.MelatowoinMod;
import net.melatowoin.registry.ModFluids;
import net.melatowoin.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ForgeChloroformFluid {

    static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MelatowoinMod.MOD_ID);

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register("chloroform",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.melatowoin.chloroform")
                    .canSwim(true)
                    .canDrown(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation("minecraft", "block/water_still");
                        private static final ResourceLocation FLOW  = new ResourceLocation("minecraft", "block/water_flow");
                        @Override public ResourceLocation getStillTexture() { return STILL; }
                        @Override public ResourceLocation getFlowingTexture() { return FLOW; }
                        @Override public int getTintColor() { return 0xA0C8F5D0; }
                    });
                }
            });

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
