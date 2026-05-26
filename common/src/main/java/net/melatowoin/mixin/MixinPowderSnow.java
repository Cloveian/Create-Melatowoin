package net.melatowoin.mixin;

import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class MixinPowderSnow {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void allowToeBeansOnSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && entity instanceof LivingEntity living) {
            if (living.getItemBySlot(EquipmentSlot.FEET).is(ModItems.TOE_BEANS.get())) {
                cir.setReturnValue(true);
            }
        }
    }
}
