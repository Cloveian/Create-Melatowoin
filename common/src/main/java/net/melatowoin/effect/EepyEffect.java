package net.melatowoin.effect;

import net.melatowoin.network.EepyScreenPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class EepyEffect extends MobEffect {

    // Fixed UUID for the speed debuff attribute modifier
    public static final UUID EEPY_SPEED_UUID = UUID.fromString("a9b8c7d6-e5f4-3210-a9b8-c7d6e5f43210");

    public EepyEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF000C);
        // Add movement speed attribute: ADD_VALUE, -255.0
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                EEPY_SPEED_UUID.toString(),
                -255.0,
                AttributeModifier.Operation.ADDITION
        );
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Handled client-side via packet; nothing to do each tick server-side
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // We do not need repeated server-side ticking
        return false;
    }

    /**
     * Called by Architectury/platform event layer when the effect is first applied.
     * Sends the open-screen packet to the client.
     */
    public static void onStarted(LivingEntity entity, MobEffectInstance instance) {
        if (entity instanceof Player player && !entity.level().isClientSide()) {
            EepyScreenPacket.sendOpen(player);
        }
    }

    /**
     * Called by the platform event layer when the effect expires or is removed.
     * Sends the close-screen packet to the client.
     * {@code instance} may be null in some platform mixin hooks.
     */
    public static void onExpired(LivingEntity entity, @org.jetbrains.annotations.Nullable MobEffectInstance instance) {
        if (entity instanceof Player player && !entity.level().isClientSide()) {
            EepyScreenPacket.sendClose(player);
        }
    }
}
