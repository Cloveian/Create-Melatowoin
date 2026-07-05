package net.melatowoin.mixin.server;

import net.melatowoin.client.CatSetCheck;
import net.melatowoin.network.SoundSourceHintPacket;
import net.melatowoin.server.ServerSoundContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Just before {@link ServerLevel} broadcasts a position-only sound, look at
 * {@link ServerSoundContext} for the player that triggered it. If set,
 * broadcast a {@link SoundSourceHintPacket} to every player within sound
 * range, mirroring vanilla's {@code PlayerList.broadcast} filter.
 *
 * The hint is sent <em>before</em> the vanilla sound packet so the client
 * processes the hint first and has it ready in its cache when the sound
 * arrives a moment later.
 *
 * Only the position-only {@code playSeededSound} overload is targeted — the
 * entity-source overload sends {@code ClientboundSoundEntityPacket} which
 * already carries the entity id and is handled by the entity-bound mixin on
 * the client side.
 */
@Mixin(ServerLevel.class)
public class MixinServerLevelPlaySound {

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V",
            at = @At("HEAD"))
    private void melatowoin$broadcastSourceHint(Player excludePlayer, double x, double y, double z,
                                                Holder<SoundEvent> sound, SoundSource cat,
                                                float volume, float pitch, long seed, CallbackInfo ci) {
        ServerPlayer acting = ServerSoundContext.get();
        if (acting == null) return;

        ResourceLocation soundId = sound.unwrapKey().map(k -> k.location()).orElse(null);
        if (soundId == null) return;

        ServerLevel self = (ServerLevel) (Object) this;
        byte flags = melatowoin$computeEquipmentFlags(acting);
        SoundSourceHintPacket hint = new SoundSourceHintPacket(acting.getId(), x, y, z, soundId, flags);

        double radius = volume > 1.0F ? 16.0 * volume : 16.0;
        double radSq = radius * radius;
        ResourceKey<Level> dim = self.dimension();

        for (ServerPlayer p : self.getServer().getPlayerList().getPlayers()) {
            if (p == excludePlayer) continue;
            if (p.level().dimension() != dim) continue;
            double dx = p.getX() - x;
            double dy = p.getY() - y;
            double dz = p.getZ() - z;
            if (dx * dx + dy * dy + dz * dz < radSq) {
                SoundSourceHintPacket.sendToPlayer(p, hint);
            }
        }
    }

    /**
     * Computes a bitmask of which cat pieces the player has equipped using
     * {@link CatSetCheck}, which consults both armor slots and the
     * platform-registered Accessories helpers — server-side, where the state
     * is authoritative and not subject to client-side sync lag.
     */
    private static byte melatowoin$computeEquipmentFlags(ServerPlayer player) {
        int flags = 0;
        if (CatSetCheck.hasCatEars(player))   flags |= SoundSourceHintPacket.FLAG_CAT_EARS;
        if (CatSetCheck.hasTail(player))      flags |= SoundSourceHintPacket.FLAG_TAIL;
        if (CatSetCheck.hasPaws(player))      flags |= SoundSourceHintPacket.FLAG_PAWS;
        if (CatSetCheck.hasToeBeans(player))  flags |= SoundSourceHintPacket.FLAG_TOE_BEANS;
        return (byte) flags;
    }
}
