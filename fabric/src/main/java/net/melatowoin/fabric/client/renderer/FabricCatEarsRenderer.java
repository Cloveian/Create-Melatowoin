package net.melatowoin.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric ArmorRenderer for cat-ears helmet (renders CatEarModel) and
 * cat-tail leggings (renders TailModel).
 */
public class FabricCatEarsRenderer implements ArmorRenderer {

    /** true = this renderer is for helmet (ears), false = leggings (tail). */
    private final boolean isHelmet;

    private CatEarModel earModel;
    private TailModel tailModel;

    public FabricCatEarsRenderer(boolean isHelmet) {
        this.isHelmet = isHelmet;
    }

    private void initModels() {
        if (earModel == null || tailModel == null) {
            EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
            earModel = new CatEarModel(modelSet.bakeLayer(CatEarModel.LAYER_LOCATION));
            tailModel = new TailModel(modelSet.bakeLayer(TailModel.LAYER_LOCATION));
        }
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers,
                       ItemStack stack, LivingEntity entity, EquipmentSlot slot,
                       int light, HumanoidModel<LivingEntity> contextModel) {
        initModels();

        if (!(stack.getItem() instanceof DyeableEquipmentItem armorItem)) return;

        matrices.pushPose();
        if (isHelmet) {
            contextModel.head.translateAndRotate(matrices);
            // Scale ears from 12 model-units wide down to 8 (matching head width).
            // Pivot at head-top (Y = -8 model units = -0.5 blocks) so ears stay on top.
            final float earScale = 8f / 12f;
            final float headTop  = 8f / 16f; // 0.5 blocks; Y- is up in model space
            matrices.translate(0.0, -headTop, 0.0);
            matrices.scale(earScale, earScale, earScale);
            matrices.translate(0.0, headTop, 0.0);
            var buffer = vertexConsumers.getBuffer(
                    RenderType.entityCutoutNoCull(new ResourceLocation(armorItem.getEarTexture())));
            earModel.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY);
        } else {
            contextModel.body.translateAndRotate(matrices);
            float partialTick = Minecraft.getInstance().getFrameTime();
            tailModel.setupAnim(entity, partialTick);
            var buffer = vertexConsumers.getBuffer(
                    RenderType.entityCutoutNoCull(new ResourceLocation(armorItem.getTailTexture())));
            tailModel.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY);
        }
        matrices.popPose();
    }
}
