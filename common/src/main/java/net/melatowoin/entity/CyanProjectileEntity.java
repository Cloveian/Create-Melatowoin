package net.melatowoin.entity;

import net.melatowoin.registry.ModEffects;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CyanProjectileEntity extends ThrowableItemProjectile {

    public CyanProjectileEntity(EntityType<? extends CyanProjectileEntity> type, Level level) {
        super(type, level);
    }

    public CyanProjectileEntity(EntityType<? extends CyanProjectileEntity> type, Level level, LivingEntity owner) {
        super(type, owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CYANABLE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffects.EEPY.get(), 140, 0));
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
