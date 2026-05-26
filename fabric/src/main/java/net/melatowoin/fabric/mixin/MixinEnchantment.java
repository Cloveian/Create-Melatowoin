package net.melatowoin.fabric.mixin;

import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MixinEnchantment {

    @Shadow public EnchantmentCategory category;

    @Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
    private void allowCosmetics(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (!(stack.getItem() instanceof DyeableEquipmentItem item)) return;

        if (category == EnchantmentCategory.ARMOR
                || category == EnchantmentCategory.BREAKABLE
                || category == EnchantmentCategory.VANISHABLE
                || category == item.slotCategory()) {
            cir.setReturnValue(true);
        }
    }
}
