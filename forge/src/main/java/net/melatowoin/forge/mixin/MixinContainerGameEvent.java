package net.melatowoin.forge.mixin;

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
 * Accessories slots): every player-sourced game event is cancelled, except a
 * short allow-list (block place/break, goat horn, fuse, explode, resonate) and
 * a STEP whose position is directly on top of a sculk sensor — so the only way
 * to trip a sensor with the full set on is to actually stand on it.
 */
@Mixin(ServerLevel.class)
public class MixinContainerGameEvent {

    @Inject(method = "gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V",
            at = @At("HEAD"), cancellable = true)
    private void melatowoin$suppressGameEvents(GameEvent event, Vec3 pos,
                                               GameEvent.Context context, CallbackInfo ci) {
        Entity source = context.sourceEntity();
        if (!(source instanceof Player player)) return;

        boolean hasEars     = melatowoin$hasCatEarsEquipped(player);
        boolean hasTail     = melatowoin$hasTailEquipped(player);
        boolean hasPaws     = melatowoin$hasPawsEquipped(player);
        boolean hasToeBeans = melatowoin$hasToeBeansEquipped(player);

        if (hasEars && hasTail && hasPaws && hasToeBeans) {
            if (melatowoin$alwaysAllowed(event)) return;
            if (event == GameEvent.STEP && melatowoin$standingOnSculkSensor((Object) source.level(), pos)) return;
            ci.cancel();
            return;
        }

        if ((event == GameEvent.CONTAINER_OPEN || event == GameEvent.CONTAINER_CLOSE) && hasPaws) {
            ci.cancel();
            return;
        }
        if ((event == GameEvent.STEP || event == GameEvent.HIT_GROUND) && hasToeBeans) {
            ci.cancel();
        }
    }

    private static boolean melatowoin$alwaysAllowed(GameEvent event) {
        return event == GameEvent.BLOCK_PLACE
            || event == GameEvent.BLOCK_DESTROY
            || event == GameEvent.INSTRUMENT_PLAY
            || event == GameEvent.PRIME_FUSE
            || event == GameEvent.EXPLODE
            || event.getName().startsWith("resonate_");
    }

    private static boolean melatowoin$standingOnSculkSensor(Object levelObj, Vec3 pos) {
        if (!(levelObj instanceof net.minecraft.world.level.Level level)) return false;
        BlockPos below = BlockPos.containing(pos.x, pos.y - 0.1, pos.z);
        if (level.getBlockState(below).getBlock() instanceof SculkSensorBlock) return true;
        BlockPos at = BlockPos.containing(pos);
        return level.getBlockState(at).getBlock() instanceof SculkSensorBlock;
    }

    private static boolean melatowoin$hasCatEarsEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.HEAD),
                DyeableEquipmentItem.EquipType.CAT_EARS)) return true;
        return melatowoin$accessoriesContainerHas(player, "hat", DyeableEquipmentItem.EquipType.CAT_EARS);
    }

    private static boolean melatowoin$hasTailEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.LEGS),
                DyeableEquipmentItem.EquipType.TAIL)) return true;
        return melatowoin$accessoriesContainerHas(player, "belt", DyeableEquipmentItem.EquipType.TAIL);
    }

    private static boolean melatowoin$hasPawsEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.CHEST),
                DyeableEquipmentItem.EquipType.PAWS)) return true;
        return melatowoin$accessoriesContainerHas(player, "hand", DyeableEquipmentItem.EquipType.PAWS);
    }

    private static boolean melatowoin$hasToeBeansEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) return true;
        return melatowoin$accessoriesContainerHas(player, "shoes", DyeableEquipmentItem.EquipType.TOE_BEANS);
    }

    private static boolean melatowoin$accessoriesContainerHas(Player player, String slotName,
                                                              DyeableEquipmentItem.EquipType type) {
        var cap = AccessoriesCapability.get(player);
        if (cap == null) return false;
        var container = cap.getContainers().get(slotName);
        if (container == null) return false;
        var stacks = container.getAccessories();
        for (int i = 0; i < stacks.getContainerSize(); i++) {
            ItemStack s = stacks.getItem(i);
            if (DyeableEquipmentItem.isType(s, type)) return true;
        }
        return false;
    }
}
