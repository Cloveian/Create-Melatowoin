package net.melatowoin.mixin.client;

import net.melatowoin.client.LocalPlayerSoundMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Marks any {@code Entity.playSound(SoundEvent, float, float)} call coming from
 * the local player so the volume-scaling mixin can identify the player's own
 * sounds without relying on the position-distance heuristic. This catches
 * client-predicted sounds (footsteps, eating, jumping, splashing, …). Sounds
 * sent by the server via {@code ClientboundSoundPacket} are handled by the
 * distance fallback in {@link MixinSoundInstanceMuffle}.
 */
@Mixin(Entity.class)
public class MixinEntityPlaySound {

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"))
    private void melatowoin$markLocalPlayer(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        if (melatowoin$isLocalPlayer()) {
            LocalPlayerSoundMarker.enter();
        }
    }

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("RETURN"))
    private void melatowoin$unmarkLocalPlayer(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        if (melatowoin$isLocalPlayer()) {
            LocalPlayerSoundMarker.exit();
        }
    }

    private boolean melatowoin$isLocalPlayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc != null && (Object) this == mc.player;
    }
}
