package net.melatowoin.entity;

import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class OrangeProjectileEntity extends ThrowableItemProjectile {

    /** Platform hook: called on hit with the struck entity and the thrown sauce stack (carries color NBT). */
    public static java.util.function.BiConsumer<LivingEntity, ItemStack> onHitExtra = (entity, stack) -> {};

    public OrangeProjectileEntity(EntityType<? extends OrangeProjectileEntity> type, Level level) {
        super(type, level);
    }

    public OrangeProjectileEntity(EntityType<? extends OrangeProjectileEntity> type, Level level, LivingEntity owner) {
        super(type, owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ORANGEABLE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity living) {
            onHitExtra.accept(living, getItem());
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }
}
