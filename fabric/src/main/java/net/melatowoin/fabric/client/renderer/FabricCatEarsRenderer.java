package net.melatowoin.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.melatowoin.client.EquipmentColorRenderer;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.PawsModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.model.ToeBeansModel;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric ArmorRenderer for all four cat equipment pieces.
 * Registered for CAT_EARS, PAWS, TAIL, and TOE_BEANS in MelatowoinFabricClient.
 *
 * Each piece is rendered in two passes (main color + accent color) using
 * EquipmentColorRenderer. Paws and toe beans render on both arms / both legs.
 */
public class FabricCatEarsRenderer implements ArmorRenderer {

    private CatEarModel   earModel;
    private TailModel     tailModel;
    private PawsModel     pawsModel;
    private ToeBeansModel toeBeansModel;

    private void initModels() {
        if (earModel != null) return;
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        earModel      = new CatEarModel(modelSet.bakeLayer(CatEarModel.LAYER_LOCATION));
        tailModel     = new TailModel(modelSet.bakeLayer(TailModel.LAYER_LOCATION));
        pawsModel     = new PawsModel(modelSet.bakeLayer(PawsModel.LAYER_LOCATION));
        toeBeansModel = new ToeBeansModel(modelSet.bakeLayer(ToeBeansModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers,
                       ItemStack stack, LivingEntity entity, EquipmentSlot slot,
                       int light, HumanoidModel<LivingEntity> contextModel) {
        if (!(stack.getItem() instanceof DyeableEquipmentItem d)) return;
        initModels();

        int main   = d.getMainColor(stack);
        int accent = d.getAccentColor(stack);

        switch (d.getEquipType()) {

            case CAT_EARS -> {
                matrices.pushPose();
                contextModel.head.translateAndRotate(matrices);
                final float earScale = 8f / 12f;
                final float headTop  = 8f / 16f;
                matrices.translate(0.0, -headTop, 0.0);
                matrices.scale(earScale, earScale, earScale);
                matrices.translate(0.0, headTop, 0.0);
                EquipmentColorRenderer.renderTwoPass(matrices, vertexConsumers, earModel,
                        d.getEntityLayer1(), d.getEntityLayer2(), main, accent, light);
                matrices.popPose();
            }

            case PAWS -> {
                float[] mc = EquipmentColorRenderer.unpack(main);
                float[] ac = EquipmentColorRenderer.unpack(accent);
                var l1 = d.getEntityLayer1();
                var l2 = d.getEntityLayer2();
                boolean slim = contextModel instanceof PlayerModel<?> pm && pm.slim;

                matrices.pushPose();
                contextModel.rightArm.translateAndRotate(matrices);
                matrices.translate(0f, -0.1f, 0f);
                matrices.scale(1.2f, 1.2f, 1.2f);
                if (slim) {
                    pawsModel.renderPawsSlim(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                    pawsModel.renderPawsSlim(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
                } else {
                    pawsModel.renderPaws(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                    pawsModel.renderPaws(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
                }
                matrices.popPose();

                matrices.pushPose();
                contextModel.leftArm.translateAndRotate(matrices);
                matrices.translate(0f, -0.1f, 0f);
                matrices.scale(-1.2f, 1.2f, 1.2f);
                if (slim) {
                    pawsModel.renderPawsSlim(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                    pawsModel.renderPawsSlim(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
                } else {
                    pawsModel.renderPaws(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                    pawsModel.renderPaws(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
                }
                matrices.popPose();
            }

            case TAIL -> {
                matrices.pushPose();
                contextModel.body.translateAndRotate(matrices);
                float partialTick = Minecraft.getInstance().getFrameTime();
                tailModel.setupAnim(entity, partialTick);
                EquipmentColorRenderer.renderTwoPass(matrices, vertexConsumers, tailModel,
                        d.getEntityLayer1(), d.getEntityLayer2(), main, accent, light);
                matrices.popPose();
            }

            case TOE_BEANS -> {
                float[] mc = EquipmentColorRenderer.unpack(main);
                float[] ac = EquipmentColorRenderer.unpack(accent);
                var l1 = d.getEntityLayer1();
                var l2 = d.getEntityLayer2();

                matrices.pushPose();
                contextModel.rightLeg.translateAndRotate(matrices);
                matrices.scale(1.2f, 1.2f, 1.2f);
                toeBeansModel.renderToeBeansThick(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                toeBeansModel.renderToeBeansThick(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
                matrices.popPose();

                matrices.pushPose();
                contextModel.leftLeg.translateAndRotate(matrices);
                matrices.scale(-1.2f, 1.2f, 1.2f);
                toeBeansModel.renderToeBeansThick(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                toeBeansModel.renderToeBeansThick(matrices, vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
                matrices.popPose();
            }
        }
    }
}
