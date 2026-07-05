package net.melatowoin.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.client.CatSetCheck;
import net.melatowoin.client.WearerConfigs;
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
 * Suppresses vanilla armor renders so the cat overlay is the only visible piece.
 *
 *   FEET  — always cancelled when Toe Beans is worn (armor or Accessories shoes).
 *   HEAD  — cancelled when Cat Ears is worn (armor or Accessories hat) AND
 *           {@link MelatowoinConfig#getHideHelmetWithCatEars()} is true.
 *   CHEST — cancelled when Paws is worn (armor or Accessories hand) AND
 *           {@link MelatowoinConfig#getHideChestplateWithPaws()} is true.
 */
@Mixin(HumanoidArmorLayer.class)
public class MixinHumanoidArmorLayer {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void melatowoin$hideArmorForCatPieces(PoseStack pose, MultiBufferSource buffers,
                                                  LivingEntity entity, EquipmentSlot slot,
                                                  int light, HumanoidModel<?> model,
                                                  CallbackInfo ci) {
        if (slot == EquipmentSlot.FEET) {
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
            return;
        }

        if (slot == EquipmentSlot.HEAD
                && entity instanceof Player player
                && WearerConfigs.get(player).hideHelmet()
                && CatSetCheck.hasCatEars(player)) {
            ci.cancel();
            return;
        }

        if (slot == EquipmentSlot.CHEST
                && entity instanceof Player player
                && WearerConfigs.get(player).hideChestplate()
                && CatSetCheck.hasPaws(player)) {
            ci.cancel();
        }
    }
}
