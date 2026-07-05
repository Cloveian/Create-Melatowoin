package net.melatowoin.entity;

import net.melatowoin.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class OrangeArrowEntity extends AbstractArrow {

    private ItemStack sauceStack = ItemStack.EMPTY;

    public OrangeArrowEntity(EntityType<? extends OrangeArrowEntity> type, Level level) {
        super(type, level);
    }

    public OrangeArrowEntity(EntityType<? extends OrangeArrowEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
    }

    public void setSauceStack(ItemStack stack) {
        this.sauceStack = stack;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide() && result.getEntity() instanceof LivingEntity living) {
            OrangeProjectileEntity.onHitExtra.accept(living, sauceStack);
            if (living instanceof net.minecraft.world.entity.player.Player target) {
                if (target instanceof net.minecraft.server.level.ServerPlayer sp) {
                    net.melatowoin.advancements.ModCriteria.HIT_BY_ORANGE.trigger(sp);
                }
                if (this.getOwner() instanceof net.minecraft.server.level.ServerPlayer shooter
                        && shooter != target) {
                    net.melatowoin.advancements.ModCriteria.APPLIED_CHANGE_TO_OTHER.trigger(shooter);
                }
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.ORANGE_ARROW.get());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!sauceStack.isEmpty()) {
            tag.put("SauceStack", sauceStack.save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SauceStack")) {
            sauceStack = ItemStack.of(tag.getCompound("SauceStack"));
        }
    }
}
