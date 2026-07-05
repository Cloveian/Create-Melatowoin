package net.melatowoin.mixin.server;

import net.melatowoin.server.ServerSoundContext;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Wraps the four user-action packet handlers on the server with set/clear
 * calls on {@link ServerSoundContext}. Any sound emitted during the handler
 * is then attributable to the player whose action it was, and the
 * {@code ServerLevel.playSeededSound} mixin broadcasts a hint packet to
 * nearby clients.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

    @Shadow public ServerPlayer player;

    // ── handleUseItem (right-click in air: bow, food, drink, throwing) ──

    @Inject(method = "handleUseItem", at = @At("HEAD"))
    private void melatowoin$enterUseItem(ServerboundUseItemPacket packet, CallbackInfo ci) {
        ServerSoundContext.enter(this.player);
    }

    @Inject(method = "handleUseItem", at = @At("RETURN"))
    private void melatowoin$exitUseItem(ServerboundUseItemPacket packet, CallbackInfo ci) {
        ServerSoundContext.exit();
    }

    // ── handleUseItemOn (right-click on block: chest, door, lever, place) ──

    @Inject(method = "handleUseItemOn", at = @At("HEAD"))
    private void melatowoin$enterUseItemOn(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
        ServerSoundContext.enter(this.player);
    }

    @Inject(method = "handleUseItemOn", at = @At("RETURN"))
    private void melatowoin$exitUseItemOn(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
        ServerSoundContext.exit();
    }

    // ── handleInteract (right-click / left-click on entity) ──

    @Inject(method = "handleInteract", at = @At("HEAD"))
    private void melatowoin$enterInteract(ServerboundInteractPacket packet, CallbackInfo ci) {
        ServerSoundContext.enter(this.player);
    }

    @Inject(method = "handleInteract", at = @At("RETURN"))
    private void melatowoin$exitInteract(ServerboundInteractPacket packet, CallbackInfo ci) {
        ServerSoundContext.exit();
    }

    // ── handlePlayerAction (block break finish, drop item, etc.) ──

    @Inject(method = "handlePlayerAction", at = @At("HEAD"))
    private void melatowoin$enterPlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        ServerSoundContext.enter(this.player);
    }

    @Inject(method = "handlePlayerAction", at = @At("RETURN"))
    private void melatowoin$exitPlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        ServerSoundContext.exit();
    }
}
