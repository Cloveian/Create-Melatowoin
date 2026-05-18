package net.melatowoin.fabric.fluid;

import net.melatowoin.registry.ModBlocks;
import net.melatowoin.registry.ModFluids;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class FabricFluids {

    public static void register() {
        // Fluid factories are lazy (called at registry freeze).
        // ModFluids.ACETONE etc. are assigned immediately after register() so
        // the lambdas can reference them — by freeze time all fields are set.

        var acetoneSource = ModFluids.FLUIDS.register("acetone",
                () -> new FabricModFluid.Source(
                        () -> (FlowingFluid) ModFluids.ACETONE.get(),
                        () -> (FlowingFluid) ModFluids.ACETONE_FLOWING.get(),
                        () -> ModBlocks.ACETONE_BLOCK.get(),
                        () -> ModItems.ACETONE_BUCKET.get()));
        var acetoneFlowing = ModFluids.FLUIDS.register("acetone_flowing",
                () -> new FabricModFluid.Flowing(
                        () -> (FlowingFluid) ModFluids.ACETONE.get(),
                        () -> (FlowingFluid) ModFluids.ACETONE_FLOWING.get(),
                        () -> ModBlocks.ACETONE_BLOCK.get(),
                        () -> ModItems.ACETONE_BUCKET.get()));
        // Block and bucket factories run at BLOCK/ITEM freeze — after FLUID freeze.
        var acetoneBlock = ModBlocks.BLOCKS.register("acetone_block",
                () -> new LiquidBlock((FlowingFluid) ModFluids.ACETONE.get(), liquidProps()));
        dev.architectury.registry.registries.RegistrySupplier<Item> acetoneBucket =
                ModItems.ITEMS.register("acetone_bucket",
                        () -> new BucketItem((FlowingFluid) ModFluids.ACETONE.get(), bucketProps()));
        ModFluids.ACETONE         = acetoneSource;
        ModFluids.ACETONE_FLOWING = acetoneFlowing;
        ModBlocks.ACETONE_BLOCK   = acetoneBlock;
        ModItems.ACETONE_BUCKET   = acetoneBucket;

        var bleachSource = ModFluids.FLUIDS.register("bleach",
                () -> new FabricModFluid.Source(
                        () -> (FlowingFluid) ModFluids.BLEACH.get(),
                        () -> (FlowingFluid) ModFluids.BLEACH_FLOWING.get(),
                        () -> ModBlocks.BLEACH_BLOCK.get(),
                        () -> ModItems.BLEACH_BUCKET.get()));
        var bleachFlowing = ModFluids.FLUIDS.register("bleach_flowing",
                () -> new FabricModFluid.Flowing(
                        () -> (FlowingFluid) ModFluids.BLEACH.get(),
                        () -> (FlowingFluid) ModFluids.BLEACH_FLOWING.get(),
                        () -> ModBlocks.BLEACH_BLOCK.get(),
                        () -> ModItems.BLEACH_BUCKET.get()));
        var bleachBlock = ModBlocks.BLOCKS.register("bleach_block",
                () -> new LiquidBlock((FlowingFluid) ModFluids.BLEACH.get(), liquidProps()));
        dev.architectury.registry.registries.RegistrySupplier<Item> bleachBucket =
                ModItems.ITEMS.register("bleach_bucket",
                        () -> new BucketItem((FlowingFluid) ModFluids.BLEACH.get(), bucketProps()));
        ModFluids.BLEACH         = bleachSource;
        ModFluids.BLEACH_FLOWING = bleachFlowing;
        ModBlocks.BLEACH_BLOCK   = bleachBlock;
        ModItems.BLEACH_BUCKET   = bleachBucket;

        var chloroformSource = ModFluids.FLUIDS.register("chloroform",
                () -> new FabricModFluid.Source(
                        () -> (FlowingFluid) ModFluids.CHLOROFORM.get(),
                        () -> (FlowingFluid) ModFluids.CHLOROFORM_FLOWING.get(),
                        () -> ModBlocks.CHLOROFORM_BLOCK.get(),
                        () -> ModItems.CHLOROFORM_BUCKET.get()));
        var chloroformFlowing = ModFluids.FLUIDS.register("chloroform_flowing",
                () -> new FabricModFluid.Flowing(
                        () -> (FlowingFluid) ModFluids.CHLOROFORM.get(),
                        () -> (FlowingFluid) ModFluids.CHLOROFORM_FLOWING.get(),
                        () -> ModBlocks.CHLOROFORM_BLOCK.get(),
                        () -> ModItems.CHLOROFORM_BUCKET.get()));
        var chloroformBlock = ModBlocks.BLOCKS.register("chloroform_block",
                () -> new LiquidBlock((FlowingFluid) ModFluids.CHLOROFORM.get(), liquidProps()));
        dev.architectury.registry.registries.RegistrySupplier<Item> chloroformBucket =
                ModItems.ITEMS.register("chloroform_bucket",
                        () -> new BucketItem((FlowingFluid) ModFluids.CHLOROFORM.get(), bucketProps()));
        ModFluids.CHLOROFORM         = chloroformSource;
        ModFluids.CHLOROFORM_FLOWING = chloroformFlowing;
        ModBlocks.CHLOROFORM_BLOCK   = chloroformBlock;
        ModItems.CHLOROFORM_BUCKET   = chloroformBucket;

        var vinegarSource = ModFluids.FLUIDS.register("vinegar",
                () -> new FabricModFluid.Source(
                        () -> (FlowingFluid) ModFluids.VINEGAR.get(),
                        () -> (FlowingFluid) ModFluids.VINEGAR_FLOWING.get(),
                        () -> ModBlocks.VINEGAR_BLOCK.get(),
                        () -> ModItems.VINEGAR_BUCKET.get()));
        var vinegarFlowing = ModFluids.FLUIDS.register("vinegar_flowing",
                () -> new FabricModFluid.Flowing(
                        () -> (FlowingFluid) ModFluids.VINEGAR.get(),
                        () -> (FlowingFluid) ModFluids.VINEGAR_FLOWING.get(),
                        () -> ModBlocks.VINEGAR_BLOCK.get(),
                        () -> ModItems.VINEGAR_BUCKET.get()));
        var vinegarBlock = ModBlocks.BLOCKS.register("vinegar_block",
                () -> new LiquidBlock((FlowingFluid) ModFluids.VINEGAR.get(), liquidProps()));
        dev.architectury.registry.registries.RegistrySupplier<Item> vinegarBucket =
                ModItems.ITEMS.register("vinegar_bucket",
                        () -> new BucketItem((FlowingFluid) ModFluids.VINEGAR.get(), bucketProps()));
        ModFluids.VINEGAR         = vinegarSource;
        ModFluids.VINEGAR_FLOWING = vinegarFlowing;
        ModBlocks.VINEGAR_BLOCK   = vinegarBlock;
        ModItems.VINEGAR_BUCKET   = vinegarBucket;
    }

    private static BlockBehaviour.Properties liquidProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .strength(100f)
                .noLootTable()
                .liquid()
                .pushReaction(PushReaction.DESTROY);
    }

    private static Item.Properties bucketProps() {
        return new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1);
    }
}
