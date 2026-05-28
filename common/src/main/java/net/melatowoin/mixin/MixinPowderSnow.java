package net.melatowoin.mixin;

import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class MixinPowderSnow {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void allowToeBeansOnSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (!(entity instanceof LivingEntity living)) return;

        // Feet armor slot
        if (living.getItemBySlot(EquipmentSlot.FEET).is(ModItems.TOE_BEANS.get())) {
            cir.setReturnValue(true);
            return;
        }

        // Accessories shoes slot (only Players have Accessories containers)
        if (living instanceof Player player) {
            ItemStack acc = AccessoriesSlotHelper.findToeBeansInAccessories.apply(player);
            if (acc.is(ModItems.TOE_BEANS.get())) {
                cir.setReturnValue(true);
            }
        }
    }
}
