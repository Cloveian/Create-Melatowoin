package net.melatowoin.forge.mixin;

import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Suppresses all footstep sounds when the entity has Toe Beans in the FEET slot. */
@Mixin(Entity.class)
public class MixinStepSound {

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void melatowoin$suppressStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!((Object) this instanceof LivingEntity self)) return;
        if (DyeableEquipmentItem.isType(self.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) {
            ci.cancel();
        }
    }
}
