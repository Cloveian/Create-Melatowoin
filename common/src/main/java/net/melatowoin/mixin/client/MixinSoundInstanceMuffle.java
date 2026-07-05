package net.melatowoin.mixin.client;

import net.melatowoin.MelatowoinConfig;
import net.melatowoin.client.CatSetCheck;
import net.melatowoin.client.ClientSoundHints;
import net.melatowoin.client.LocalPlayerSoundMarker;
import net.melatowoin.client.SoundKind;
import net.melatowoin.network.SoundSourceHintPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Scales the volume of player-sourced sounds while wearing the matching cat
 * pieces, governed by {@link MelatowoinConfig#getFullSetSoundReduction()}.
 *
 * Source identification — in order of trust:
 *   1. {@link ClientSoundHints} — authoritative server hint identifying the
 *      causing entity. Sent by the server alongside the standard sound packet
 *      for any sound triggered inside a player action handler.
 *   2. {@link LocalPlayerSoundMarker#isNotLocal()} — packet-level marker for
 *      entity-bound sound packets whose entity id is not us.
 *   3. {@link LocalPlayerSoundMarker#isLocal()}    — packet-level marker for
 *      entity-bound sound packets whose entity id is us, or the local
 *      {@code Entity.playSound} emit chain.
 *   4. Position-only fallback: within ~2 blocks of the player AND no other
 *      LivingEntity sitting at the sound position.
 */
@Mixin(AbstractSoundInstance.class)
public class MixinSoundInstanceMuffle {

    @Inject(method = "getVolume", at = @At("RETURN"), cancellable = true)
    private void melatowoin$catMuffle(CallbackInfoReturnable<Float> cir) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        double reduction = MelatowoinConfig.getFullSetSoundReduction();
        if (reduction <= 0.0) return;

        SoundInstance self = (SoundInstance) (Object) this;
        ResourceLocation soundLoc = self.getLocation();
        if (soundLoc == null) return;

        boolean knownLocal = false;
        ClientSoundHints.Hint hint = ClientSoundHints.lookup(self.getX(), self.getY(), self.getZ(), soundLoc);

        // 1. Server-authoritative hint.
        if (hint != null) {
            if (hint.sourceEntityId() != player.getId()) return;  // server says it's someone else
            knownLocal = true;
        }

        // 2. Entity-bound packet says it's not us.
        if (!knownLocal && LocalPlayerSoundMarker.isNotLocal()) return;

        // 3. Entity-bound packet or client emit chain says it's us.
        if (!knownLocal && LocalPlayerSoundMarker.isLocal()) {
            knownLocal = true;
        }

        // 4. Position-only fallback for sounds the server didn't hint about
        //    (e.g. levelEvent-driven sounds, redstone-driven sounds at the
        //    player's position).
        if (!knownLocal) {
            double dx = self.getX() - player.getX();
            double dy = self.getY() - player.getY();
            double dz = self.getZ() - player.getZ();
            if (dx * dx + dy * dy + dz * dz > 4.0) return;

            Level level = player.level();
            if (level != null) {
                AABB box = new AABB(
                        self.getX() - 0.5, self.getY() - 0.5, self.getZ() - 0.5,
                        self.getX() + 0.5, self.getY() + 0.5, self.getZ() + 0.5);
                if (!level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player).isEmpty()) {
                    return;
                }
            }
        }

        SoundKind kind = classify(soundLoc.getPath());
        if (kind == SoundKind.EXCLUDED) return;

        // Server hint carries the wearer's equipment snapshot; use it where present
        // to bypass any client-side Accessories sync lag. Fall back to local
        // CatSetCheck for unhinted (distance-fallback) cases.
        boolean reduce;
        if (hint != null) {
            switch (kind) {
                case WALKING -> reduce = hint.hasFlag(SoundSourceHintPacket.FLAG_TOE_BEANS);
                case HAND    -> reduce = hint.hasFlag(SoundSourceHintPacket.FLAG_PAWS);
                case OTHER   -> reduce = hint.hasFlag(SoundSourceHintPacket.FLAG_CAT_EARS)
                                     && hint.hasFlag(SoundSourceHintPacket.FLAG_TAIL)
                                     && hint.hasFlag(SoundSourceHintPacket.FLAG_PAWS)
                                     && hint.hasFlag(SoundSourceHintPacket.FLAG_TOE_BEANS);
                default      -> reduce = false;
            }
        } else {
            switch (kind) {
                case WALKING -> reduce = CatSetCheck.hasToeBeans(player);
                case HAND    -> reduce = CatSetCheck.hasPaws(player);
                case OTHER   -> reduce = CatSetCheck.hasFullSet(player);
                default      -> reduce = false;
            }
        }
        if (!reduce) return;

        float vol = cir.getReturnValueF();
        cir.setReturnValue((float) (vol * (1.0 - reduction)));
    }

    private static SoundKind classify(String path) {
        // ── EXCLUDED ──
        if (path.startsWith("item.goat_horn.")) return SoundKind.EXCLUDED;
        if (path.contains("tnt.primed") || path.endsWith(".fuse")) return SoundKind.EXCLUDED;
        if (path.contains("explode") || path.contains(".blast")) return SoundKind.EXCLUDED;
        if (path.contains("resonate") || path.startsWith("block.bell.")) return SoundKind.EXCLUDED;
        if (path.startsWith("block.")
                && (path.endsWith(".place") || path.endsWith(".break") || path.endsWith(".destroy"))) {
            return SoundKind.EXCLUDED;
        }

        // ── WALKING — Toe Beans ──
        if (path.endsWith(".step")) return SoundKind.WALKING;
        if (path.contains(".splash") || path.contains(".swim")) return SoundKind.WALKING;
        if (path.contains(".fall") || path.contains("jump")) return SoundKind.WALKING;

        // ── HAND — Paws ──
        if (path.endsWith(".open") || path.endsWith(".close")) return SoundKind.HAND;
        if (path.endsWith(".eat") || path.endsWith(".drink")) return SoundKind.HAND;
        if (path.contains("generic.eat") || path.contains("generic.drink")) return SoundKind.HAND;
        if (path.equals("entity.player.burp")) return SoundKind.HAND;

        return SoundKind.OTHER;
    }
}
