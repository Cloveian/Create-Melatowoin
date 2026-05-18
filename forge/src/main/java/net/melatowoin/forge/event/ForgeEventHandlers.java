package net.melatowoin.forge.event;

import net.melatowoin.MelatowoinMod;
import net.melatowoin.effect.ChangeEffect;
import net.melatowoin.effect.EepyEffect;
import net.melatowoin.registry.ModEffects;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge game-bus event handlers.
 *
 * Cat brushing is handled by Architectury's cross-platform InteractionEvent
 * (registered in CommonEventHandlers). These handlers cover Forge-specific
 * events that have no Architectury equivalent: mob-effect lifecycle.
 */
@Mod.EventBusSubscriber(modid = MelatowoinMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandlers {

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance() == null) return;
        if (event.getEffectInstance().getEffect() == ModEffects.EEPY.get()) {
            EepyEffect.onStarted(event.getEntity(), event.getEffectInstance());
        } else if (event.getEffectInstance().getEffect() == ModEffects.CHANGE.get()) {
            ChangeEffect.onStarted(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onMobEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() == null) return;
        if (event.getEffectInstance().getEffect() == ModEffects.EEPY.get()) {
            EepyEffect.onExpired(event.getEntity(), event.getEffectInstance());
        }
    }

    @SubscribeEvent
    public static void onMobEffectRemoved(MobEffectEvent.Remove event) {
        // event.getEffectInstance() returns the MobEffectInstance being removed
        var instance = event.getEffectInstance();
        if (instance == null) return;
        if (instance.getEffect() == ModEffects.EEPY.get()) {
            EepyEffect.onExpired(event.getEntity(), instance);
        }
    }
}
