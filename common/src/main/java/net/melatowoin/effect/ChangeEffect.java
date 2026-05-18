package net.melatowoin.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ChangeEffect extends MobEffect {

    public ChangeEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFFB000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Not used — behavior is driven by applyEffect called on start
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    public static void onStarted(LivingEntity entity) {
        // Equipping is handled by OrangeProjectileEntity.onHitExtra before the effect is applied.
    }
}
