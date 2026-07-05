package net.melatowoin.fabric.mixin;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts game events at the ServerLevel dispatch point (before any GameEventListener
 * is notified) and suppresses Player-sourced events according to the cat outfit.
 *
 * Partial set:
 *   Paws       → CONTAINER_OPEN / CONTAINER_CLOSE
 *   Toe Beans  → STEP / HIT_GROUND
 *
 * Full set (Cat Ears + Tail + Paws + Toe Beans, in any combination of armor and
 * Accessories slots): EVERY player-sourced game event is cancelled, so sculk
 * sensors and the Warden cannot pick up walking, jumping, swimming, opening
 * doors, breaking blocks, eating, drinking, shooting bows, exploding TNT,
 * teleporting, equipping armor, ringing bells, playing note blocks, etc.
 * The single exception is a STEP whose position is directly on top of a sculk
 * sensor block — so "step on the sensor itself" remains the only way to
 * trigger one while wearing the full set.
 */
@Mixin(ServerLevel.class)
public class MixinContainerGameEvent {

    @Inject(method = "method_32888", remap = false, at = @At("HEAD"), cancellable = true)
    private void melatowoin$suppressGameEvents(GameEvent event, Vec3 pos,
                                               GameEvent.Context context, CallbackInfo ci) {
        Entity source = context.sourceEntity();
        if (!(source instanceof Player player)) return;

        boolean hasEars     = melatowoin$hasCatEarsEquipped(player);
        boolean hasTail     = melatowoin$hasTailEquipped(player);
        boolean hasPaws     = melatowoin$hasPawsEquipped(player);
        boolean hasToeBeans = melatowoin$hasToeBeansEquipped(player);

        if (hasEars && hasTail && hasPaws && hasToeBeans) {
            // Full set: suppress everything except a small allow-list and STEP on a sensor.
            if (melatowoin$alwaysAllowed(event)) return;
            if (event == GameEvent.STEP && melatowoin$standingOnSculkSensor((Object) source.level(), pos)) return;
            ci.cancel();
            if (event == GameEvent.STEP && player instanceof net.minecraft.server.level.ServerPlayer sp
                    && melatowoin$sculkListenerNearby(sp, pos)) {
                net.melatowoin.advancements.ModCriteria.SILENT_SCULK_PASS.trigger(sp);
            }
            return;
        }

        if ((event == GameEvent.CONTAINER_OPEN || event == GameEvent.CONTAINER_CLOSE) && hasPaws) {
            ci.cancel();
            if (event == GameEvent.CONTAINER_OPEN && player instanceof net.minecraft.server.level.ServerPlayer sp) {
                net.melatowoin.advancements.ModCriteria.SILENT_CHEST_OPEN.trigger(sp);
            }
            return;
        }
        if ((event == GameEvent.STEP || event == GameEvent.HIT_GROUND) && hasToeBeans) {
            ci.cancel();
            if (event == GameEvent.STEP && player instanceof net.minecraft.server.level.ServerPlayer sp
                    && melatowoin$sculkListenerNearby(sp, pos)) {
                net.melatowoin.advancements.ModCriteria.SILENT_SCULK_PASS.trigger(sp);
            }
        }
    }

    /**
     * Game events that are NEVER suppressed by the full set:
     *  - BLOCK_PLACE / BLOCK_DESTROY  (place/break still trigger sensors)
     *  - INSTRUMENT_PLAY              (goat horn)
     *  - PRIME_FUSE                   (lighting TNT)
     *  - EXPLODE                      (explosion)
     *  - RESONATE_*                   (bell-on-resonating-block ripple)
     */
    private static boolean melatowoin$alwaysAllowed(GameEvent event) {
        return event == GameEvent.BLOCK_PLACE
            || event == GameEvent.BLOCK_DESTROY
            || event == GameEvent.INSTRUMENT_PLAY
            || event == GameEvent.PRIME_FUSE
            || event == GameEvent.EXPLODE
            // SCULK_SENSOR_TENDRILS_CLICKING: a sensor's broadcast that lets nearby
            // shriekers react to the same vibration. SHRIEK: the shrieker's own
            // propagation. Both carry the original player as source, so without
            // these the sensor → shrieker → warden chain breaks for full-set wearers.
            || event == GameEvent.SCULK_SENSOR_TENDRILS_CLICKING
            || event == GameEvent.SHRIEK
            || event.getName().startsWith("resonate_");
    }

    private static boolean melatowoin$standingOnSculkSensor(Object levelObj, Vec3 pos) {
        if (!(levelObj instanceof net.minecraft.world.level.Level level)) return false;
        BlockPos below = BlockPos.containing(pos.x, pos.y - 0.1, pos.z);
        if (level.getBlockState(below).getBlock() instanceof SculkSensorBlock) return true;
        // Also accept STEP firing exactly inside the sensor block (shouldn't normally happen, but safe).
        BlockPos at = BlockPos.containing(pos);
        return level.getBlockState(at).getBlock() instanceof SculkSensorBlock;
    }

    /** Cheap chunk-local scan for sculk sensors/shriekers — used to gate the silent-sculk advancement. */
    private static boolean melatowoin$sculkListenerNearby(net.minecraft.server.level.ServerPlayer sp, Vec3 pos) {
        net.minecraft.world.level.Level level = sp.level();
        for (var be : level.getChunkAt(BlockPos.containing(pos)).getBlockEntities().values()) {
            if (be instanceof net.minecraft.world.level.block.entity.SculkSensorBlockEntity
                    || be instanceof net.minecraft.world.level.block.entity.SculkShriekerBlockEntity) {
                if (be.getBlockPos().distSqr(BlockPos.containing(pos)) <= 64.0) return true;
            }
        }
        return false;
    }

    private static boolean melatowoin$hasCatEarsEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.HEAD),
                DyeableEquipmentItem.EquipType.CAT_EARS)) return true;
        return melatowoin$accessoriesHas(player, DyeableEquipmentItem.EquipType.CAT_EARS);
    }

    private static boolean melatowoin$hasTailEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.LEGS),
                DyeableEquipmentItem.EquipType.TAIL)) return true;
        return melatowoin$accessoriesHas(player, DyeableEquipmentItem.EquipType.TAIL);
    }

    private static boolean melatowoin$hasPawsEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.CHEST),
                DyeableEquipmentItem.EquipType.PAWS)) return true;
        return melatowoin$accessoriesHas(player, DyeableEquipmentItem.EquipType.PAWS);
    }

    private static boolean melatowoin$hasToeBeansEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) return true;
        return melatowoin$accessoriesHas(player, DyeableEquipmentItem.EquipType.TOE_BEANS);
    }

    /** Scans every Accessories container for the given cat-piece type, regardless of slot name. */
    private static boolean melatowoin$accessoriesHas(Player player, DyeableEquipmentItem.EquipType type) {
        var cap = AccessoriesCapability.get(player);
        if (cap == null) return false;
        for (var container : cap.getContainers().values()) {
            var stacks = container.getAccessories();
            for (int i = 0; i < stacks.getContainerSize(); i++) {
                ItemStack s = stacks.getItem(i);
                if (DyeableEquipmentItem.isType(s, type)) return true;
            }
        }
        return false;
    }
}
