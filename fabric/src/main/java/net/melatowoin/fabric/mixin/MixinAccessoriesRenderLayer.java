package net.melatowoin.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import io.wispforest.accessories.client.AccessoriesRenderLayer;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Suppresses other mods' accessory renderers in any glove-style Accessories
 * slot whenever the entity is wearing Paws (in the chest armor slot or in
 * the Accessories "hand" slot). Aether ships an "aether_gloves" slot; other
 * mods may use names containing "glove" or the base "hand" slot.
 */
@Mixin(AccessoriesRenderLayer.class)
public class MixinAccessoriesRenderLayer {

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lio/wispforest/accessories/api/client/AccessoryRenderer;render(Lnet/minecraft/world/item/ItemStack;Lio/wispforest/accessories/api/slot/SlotReference;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/model/EntityModel;Lnet/minecraft/client/renderer/MultiBufferSource;IFFFFFF)V"))
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void melatowoin$hideGlovesWhenPawsWorn(
            AccessoryRenderer self,
            ItemStack stack, SlotReference reference, PoseStack pose,
            EntityModel model, MultiBufferSource buffers, int light,
            float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {
        if (shouldSkip(stack, reference)) return;
        self.render(stack, reference, pose, model, buffers, light,
                limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch);
    }

    private static boolean shouldSkip(ItemStack stack, SlotReference ref) {
        if (!isGloveLikeSlot(ref.slotName())) return false;
        if (stack.getItem() instanceof DyeableEquipmentItem d
                && d.getEquipType() == DyeableEquipmentItem.EquipType.PAWS) {
            return false;
        }
        return entityWearsPaws(ref.entity());
    }

    private static boolean isGloveLikeSlot(String slotName) {
        if (slotName == null) return false;
        if ("hand".equals(slotName)) return true;
        return slotName.toLowerCase(java.util.Locale.ROOT).contains("glove");
    }

    private static boolean entityWearsPaws(LivingEntity entity) {
        if (DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.CHEST),
                DyeableEquipmentItem.EquipType.PAWS)) {
            return true;
        }
        if (entity instanceof Player player) {
            ItemStack acc = AccessoriesSlotHelper.findPawsInAccessories.apply(player);
            return DyeableEquipmentItem.isType(acc, DyeableEquipmentItem.EquipType.PAWS);
        }
        return false;
    }
}
