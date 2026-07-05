package net.melatowoin.forge.mixin;

import net.melatowoin.client.LocalPlayerSoundMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Marks {@link LocalPlayerSoundMarker} around the entity-bound sound packet
 * handler so the volume mixin can tell whether a sound is from the local
 * player or another entity. Forge variant pins the SRG method name with
 * {@code remap = false} because the Mojmap name in 1.20.1 is
 * {@code handleSoundEntityEvent} and Loom's refmap generation for this class
 * wasn't producing the translation entry.
 *
 *   m_5863_ = ClientPacketListener.handleSoundEntityEvent(ClientboundSoundEntityPacket)
 */
@Mixin(ClientPacketListener.class)
public class MixinClientPacketListenerSound {

    @Inject(method = "m_5863_", remap = false, at = @At("HEAD"))
    private void melatowoin$markEntitySoundBefore(ClientboundSoundEntityPacket packet, CallbackInfo ci) {
        if (melatowoin$isLocalPlayerId(packet.getId())) {
            LocalPlayerSoundMarker.enterLocal();
        } else {
            LocalPlayerSoundMarker.enterNotLocal();
        }
    }

    @Inject(method = "m_5863_", remap = false, at = @At("RETURN"))
    private void melatowoin$markEntitySoundAfter(ClientboundSoundEntityPacket packet, CallbackInfo ci) {
        if (melatowoin$isLocalPlayerId(packet.getId())) {
            LocalPlayerSoundMarker.exitLocal();
        } else {
            LocalPlayerSoundMarker.exitNotLocal();
        }
    }

    private static boolean melatowoin$isLocalPlayerId(int id) {
        Minecraft mc = Minecraft.getInstance();
        return mc != null && mc.player != null && mc.player.getId() == id;
    }
}
