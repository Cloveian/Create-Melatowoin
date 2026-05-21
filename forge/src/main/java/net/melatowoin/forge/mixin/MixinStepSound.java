package net.melatowoin.forge.mixin;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Suppresses all footstep sounds when the entity has Toe Beans in the FEET or Accessories shoes slot. */
@Mixin(Entity.class)
public class MixinStepSound {

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void melatowoin$suppressStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!((Object) this instanceof LivingEntity self)) return;
        if (melatowoin$hasToeBeansEquipped(self)) ci.cancel();
    }

    private static boolean melatowoin$hasToeBeansEquipped(LivingEntity entity) {
        if (DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) return true;
        var cap = AccessoriesCapability.get(entity);
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
