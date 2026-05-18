package net.melatowoin.fabric.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class FabricModFluid extends FlowingFluid {

    private final Supplier<FlowingFluid> source;
    private final Supplier<FlowingFluid> flowing;
    private final Supplier<Block> block;
    private final Supplier<Item> bucket;

    protected FabricModFluid(Supplier<FlowingFluid> source, Supplier<FlowingFluid> flowing,
                             Supplier<Block> block, Supplier<Item> bucket) {
        this.source  = source;
        this.flowing = flowing;
        this.block   = block;
        this.bucket  = bucket;
    }

    @Override public FlowingFluid getSource() { return source.get(); }
    @Override public FlowingFluid getFlowing() { return flowing.get(); }
    @Override public boolean isSame(Fluid fluid) { return fluid == source.get() || fluid == flowing.get(); }
    @Override public Item getBucket() { return bucket.get(); }
    @Override protected BlockState createLegacyBlock(FluidState state) {
        return block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }
    @Override protected boolean canConvertToSource(Level level) { return false; }
    @Override protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {}
    @Override protected int getSlopeFindDistance(LevelReader world) { return 4; }
    @Override protected int getDropOff(LevelReader world) { return 1; }
    @Override public int getTickDelay(LevelReader world) { return 5; }
    @Override protected float getExplosionResistance() { return 100f; }
    @Override protected boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos,
                                                  Fluid fluid, Direction direction) { return false; }
    @Override public Optional<SoundEvent> getPickupSound() { return Optional.of(SoundEvents.BUCKET_FILL); }

    // ---- Source ----

    public static class Source extends FabricModFluid {
        public Source(Supplier<FlowingFluid> source, Supplier<FlowingFluid> flowing,
                      Supplier<Block> block, Supplier<Item> bucket) {
            super(source, flowing, block, bucket);
        }

        @Override public boolean isSource(FluidState state) { return true; }
        @Override public int getAmount(FluidState state) { return 8; }
    }

    // ---- Flowing ----

    public static class Flowing extends FabricModFluid {
        public Flowing(Supplier<FlowingFluid> source, Supplier<FlowingFluid> flowing,
                       Supplier<Block> block, Supplier<Item> bucket) {
            super(source, flowing, block, bucket);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override public boolean isSource(FluidState state) { return false; }
        @Override public int getAmount(FluidState state) { return state.getValue(LEVEL); }
    }
}
