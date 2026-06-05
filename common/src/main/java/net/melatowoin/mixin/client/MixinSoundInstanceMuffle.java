package net.melatowoin.mixin.client;

import net.melatowoin.MelatowoinConfig;
import net.melatowoin.client.CatSetCheck;
import net.melatowoin.client.LocalPlayerSoundMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Scales the volume of player-sourced sounds while wearing the matching cat
 * pieces, governed by {@link MelatowoinConfig#getFullSetSoundReduction()}.
 *
 *  - WALKING-class sounds (step, swim, splash, fall, jump): muted by Toe Beans.
 *  - HAND-class sounds (open/close, eat/drink, burp):       muted by Paws.
 *  - OTHER sounds (hurt, pickup, arrow shoot, level-up …):  muted only by the
 *    full set.
 *  - EXCLUDED sounds (place/break, goat horn, fuse, explode, blast, resonate,
 *    bell) are NEVER muted — they intentionally line up with the game events
 *    that still trigger sculk sensors through the full set.
 *
 * Only sounds within ~2 blocks of the local player are touched, so other
 * players' / mobs' actions are unaffected.
 */
@Mixin(AbstractSoundInstance.class)
public class MixinSoundInstanceMuffle {

    private enum Kind { WALKING, HAND, EXCLUDED, OTHER }

    @Inject(method = "getVolume", at = @At("RETURN"), cancellable = true)
    private void melatowoin$catMuffle(CallbackInfoReturnable<Float> cir) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        double reduction = MelatowoinConfig.getFullSetSoundReduction();
        if (reduction <= 0.0) return;

        SoundInstance self = (SoundInstance) (Object) this;
        // Definite-source path: we're inside Entity.playSound called by the local
        // player (footsteps, eating, jumping, …). Distance is irrelevant.
        // Fallback: position within ~2 blocks of the player, for server-sent
        // sounds (hurt, level-up, etc.) that can't be tagged at emit time.
        if (!LocalPlayerSoundMarker.active()) {
            double dx = self.getX() - player.getX();
            double dy = self.getY() - player.getY();
            double dz = self.getZ() - player.getZ();
            if (dx * dx + dy * dy + dz * dz > 4.0) return;
        }

        ResourceLocation loc = self.getLocation();
        if (loc == null) return;
        Kind kind = classify(loc.getPath());
        if (kind == Kind.EXCLUDED) return;

        boolean reduce;
        switch (kind) {
            case WALKING -> reduce = CatSetCheck.hasToeBeans(player);
            case HAND    -> reduce = CatSetCheck.hasPaws(player);
            case OTHER   -> reduce = CatSetCheck.hasFullSet(player);
            default      -> reduce = false;
        }
        if (!reduce) return;

        float vol = cir.getReturnValueF();
        cir.setReturnValue((float) (vol * (1.0 - reduction)));
    }

    private static Kind classify(String path) {
        // ── EXCLUDED — matches the GameEvent allow-list so sounds match sculk behavior ──
        if (path.startsWith("item.goat_horn.")) return Kind.EXCLUDED;
        if (path.contains("tnt.primed") || path.endsWith(".fuse")) return Kind.EXCLUDED;
        if (path.contains("explode") || path.contains(".blast")) return Kind.EXCLUDED;
        if (path.contains("resonate") || path.startsWith("block.bell.")) return Kind.EXCLUDED;
        if (path.startsWith("block.")
                && (path.endsWith(".place") || path.endsWith(".break") || path.endsWith(".destroy"))) {
            return Kind.EXCLUDED;
        }

        // ── WALKING — Toe Beans ──
        if (path.endsWith(".step")) return Kind.WALKING;
        if (path.contains(".splash") || path.contains(".swim")) return Kind.WALKING;
        if (path.contains(".fall") || path.contains("jump")) return Kind.WALKING;

        // ── HAND — Paws ──
        if (path.endsWith(".open") || path.endsWith(".close")) return Kind.HAND;
        if (path.endsWith(".eat") || path.endsWith(".drink")) return Kind.HAND;
        if (path.contains("generic.eat") || path.contains("generic.drink")) return Kind.HAND;
        if (path.equals("entity.player.burp")) return Kind.HAND;

        return Kind.OTHER;
    }
}
