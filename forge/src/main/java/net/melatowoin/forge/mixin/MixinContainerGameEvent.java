package net.melatowoin.forge.mixin;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts game events at the ServerLevel dispatch point (before any GameEventListener
 * is notified) and suppresses Paws/Toe Beans events so sculk sensors and the Warden
 * cannot detect them.
 */
@Mixin(ServerLevel.class)
public class MixinContainerGameEvent {

    @Inject(method = "gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V",
            at = @At("HEAD"), cancellable = true)
    private void melatowoin$suppressGameEvents(GameEvent event, Vec3 pos,
                                               GameEvent.Context context, CallbackInfo ci) {
        Entity source = context.sourceEntity();
        if (!(source instanceof Player player)) return;
        if ((event == GameEvent.CONTAINER_OPEN || event == GameEvent.CONTAINER_CLOSE)
                && melatowoin$hasPawsEquipped(player)) {
            ci.cancel();
            return;
        }
        if ((event == GameEvent.STEP || event == GameEvent.HIT_GROUND)
                && melatowoin$hasToeBeansEquipped(player)) {
            ci.cancel();
        }
    }

    private static boolean melatowoin$hasPawsEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.CHEST),
                DyeableEquipmentItem.EquipType.PAWS)) return true;
        var cap = AccessoriesCapability.get(player);
        if (cap == null) return false;
        var container = cap.getContainers().get("hand");
        if (container == null) return false;
        var stacks = container.getAccessories();
        for (int i = 0; i < stacks.getContainerSize(); i++) {
            ItemStack s = stacks.getItem(i);
            if (DyeableEquipmentItem.isType(s, DyeableEquipmentItem.EquipType.PAWS)) return true;
        }
        return false;
    }

    private static boolean melatowoin$hasToeBeansEquipped(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) return true;
        var cap = AccessoriesCapability.get(player);
        if (cap == null) return false;
        var container = cap.getContainers().get("shoes");
        if (container == null) return false;
        var stacks = container.getAccessories();
        for (int i = 0; i < stacks.getContainerSize(); i++) {
            ItemStack s = stacks.getItem(i);
            if (DyeableEquipmentItem.isType(s, DyeableEquipmentItem.EquipType.TOE_BEANS)) return true;
        }
        return false;
    }
}
