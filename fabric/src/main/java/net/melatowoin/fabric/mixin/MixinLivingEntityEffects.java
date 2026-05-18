package net.melatowoin.fabric.mixin;

import net.melatowoin.effect.ChangeEffect;
import net.melatowoin.effect.EepyEffect;
import net.melatowoin.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Mixin on LivingEntity to hook mob-effect applied/removed events on Fabric,
 * where Fabric API (0.91.x) does not yet expose mob-effect lifecycle callbacks.
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntityEffects {

    /**
     * Fires after a new effect is successfully added or its duration is extended.
     * We only care about the first-time application (when the effect was not already active).
     */
    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("RETURN"))
    private void melatowoin$onEffectAdded(MobEffectInstance effectInstance,
                                          net.minecraft.world.entity.Entity source,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return; // effect was not applied

        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) return;

        if (effectInstance.getEffect() == ModEffects.EEPY.get()) {
            EepyEffect.onStarted(self, effectInstance);
        } else if (effectInstance.getEffect() == ModEffects.CHANGE.get()) {
            ChangeEffect.onStarted(self);
        }
    }

    /**
     * Fires when removeEffect is called (manual removal or expiry).
     */
    @Inject(method = "removeEffect(Lnet/minecraft/world/effect/MobEffect;)Z",
            at = @At("RETURN"))
    private void melatowoin$onEffectRemoved(net.minecraft.world.effect.MobEffect effect,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) return;

        if (effect == ModEffects.EEPY.get()) {
            // Reconstruct a dummy instance for the callback signature
            EepyEffect.onExpired(self, null);
        }
    }
}
