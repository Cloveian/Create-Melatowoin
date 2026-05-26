package net.melatowoin.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides vanilla feet-slot armor render when the entity is wearing Toe Beans.
 * Covers the armor-slot case (so the player's vanilla boot model never draws
 * underneath the cat overlay) and the Accessories feet-slot case (where real
 * iron/leather boots would otherwise clip through the Toe Beans render).
 */
@Mixin(HumanoidArmorLayer.class)
public class MixinHumanoidArmorLayer {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void melatowoin$hideBootsForToeBeans(PoseStack pose, MultiBufferSource buffers,
                                                 LivingEntity entity, EquipmentSlot slot,
                                                 int light, HumanoidModel<?> model,
                                                 CallbackInfo ci) {
        if (slot != EquipmentSlot.FEET) return;

        if (DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) {
            ci.cancel();
            return;
        }

        if (entity instanceof Player player) {
            ItemStack acc = AccessoriesSlotHelper.findToeBeansInAccessories.apply(player);
            if (DyeableEquipmentItem.isType(acc, DyeableEquipmentItem.EquipType.TOE_BEANS)) {
                ci.cancel();
            }
        }
    }
}
