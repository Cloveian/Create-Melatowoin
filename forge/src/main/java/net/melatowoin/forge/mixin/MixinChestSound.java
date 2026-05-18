package net.melatowoin.forge.mixin;

import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Suppresses chest open/close sounds on the client when the local player has Paws equipped. */
@Mixin(ClientPacketListener.class)
public class MixinChestSound {

    @Inject(method = "handleSound", at = @At("HEAD"), cancellable = true)
    private void melatowoin$suppressChestSound(ClientboundSoundPacket packet, CallbackInfo ci) {
        var sound = packet.getSound().value();
        if (sound != SoundEvents.CHEST_OPEN && sound != SoundEvents.CHEST_CLOSE) return;
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (DyeableEquipmentItem.isType(mc.player.getItemBySlot(EquipmentSlot.CHEST),
                DyeableEquipmentItem.EquipType.PAWS)) {
            ci.cancel();
        }
    }
}
