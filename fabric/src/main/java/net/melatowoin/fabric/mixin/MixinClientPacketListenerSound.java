package net.melatowoin.fabric.mixin;

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
 * player or another entity. Fabric variant uses the intermediary method name
 * directly because Loom's common-module refmap cannot translate the Mojmap
 * method name in this case.
 *
 *   method_11125 = ClientPacketListener.handleSoundEntity(ClientboundSoundEntityPacket)
 */
@Mixin(ClientPacketListener.class)
public class MixinClientPacketListenerSound {

    @Inject(method = "method_11125", remap = false, at = @At("HEAD"))
    private void melatowoin$markEntitySoundBefore(ClientboundSoundEntityPacket packet, CallbackInfo ci) {
        if (melatowoin$isLocalPlayerId(packet.getId())) {
            LocalPlayerSoundMarker.enterLocal();
        } else {
            LocalPlayerSoundMarker.enterNotLocal();
        }
    }

    @Inject(method = "method_11125", remap = false, at = @At("RETURN"))
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
