package net.melatowoin.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.melatowoin.client.EquipmentColorRenderer;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.PawsModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.model.ToeBeansModel;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AccessoriesIntegration {

    public static void registerRenderers() {
        AccessoriesRendererRegistry.registerRenderer(ModItems.CAT_EARS.get(),  EarAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.TAIL.get(),      TailAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.PAWS.get(),      PawsAccessoryRenderer::new);
        AccessoriesRendererRegistry.registerRenderer(ModItems.TOE_BEANS.get(), ToeBeansAccessoryRenderer::new);
    }

    private static class EarAccessoryRenderer implements AccessoryRenderer {
        private CatEarModel earModel;

        @Override public boolean shouldRender(boolean isRendering) { return true; }

        private void initModel() {
            if (earModel == null) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                earModel = new CatEarModel(modelSet.bakeLayer(CatEarModel.LAYER_LOCATION));
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public <M extends LivingEntity> void render(
                ItemStack stack, SlotReference reference, PoseStack matrices,
                EntityModel<M> model, MultiBufferSource buffers,
                int light, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(stack.getItem() instanceof DyeableEquipmentItem d)) return;
            initModel();

            matrices.pushPose();
            ((HumanoidModel<?>) model).head.translateAndRotate(matrices);
            final float earScale = 8f / 12f;
            final float headTop  = 8f / 16f;
            // Shift ears 1 pixel higher when a helmet occupies the head armor slot
            // so the ears sit on top of the helmet instead of clipping through it.
            float helmetLift = reference.entity().getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).isEmpty()
                    ? 0f : 1f / 16f;
            matrices.translate(0.0, -headTop - helmetLift, 0.0);
            matrices.scale(earScale, earScale, earScale);
            matrices.translate(0.0, headTop, 0.0);
            EquipmentColorRenderer.renderTwoPass(matrices, buffers, earModel,
                    d.getEntityLayer1(), d.getEntityLayer2(),
                    d.getMainColor(stack), d.getAccentColor(stack), light);
            matrices.popPose();
        }
    }

    private static class TailAccessoryRenderer implements AccessoryRenderer {
        private TailModel tailModel;

        @Override public boolean shouldRender(boolean isRendering) { return true; }

        private void initModel() {
            if (tailModel == null) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                tailModel = new TailModel(modelSet.bakeLayer(TailModel.LAYER_LOCATION));
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public <M extends LivingEntity> void render(
                ItemStack stack, SlotReference reference, PoseStack matrices,
                EntityModel<M> model, MultiBufferSource buffers,
                int light, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(stack.getItem() instanceof DyeableEquipmentItem d)) return;
            initModel();

            matrices.pushPose();
            ((HumanoidModel<?>) model).body.translateAndRotate(matrices);
            tailModel.setupAnim(reference.entity(), partialTicks);
            EquipmentColorRenderer.renderTwoPass(matrices, buffers, tailModel,
                    d.getEntityLayer1(), d.getEntityLayer2(),
                    d.getMainColor(stack), d.getAccentColor(stack), light);
            matrices.popPose();
        }
    }

    private static class PawsAccessoryRenderer implements AccessoryRenderer {
        private PawsModel pawsModel;

        @Override public boolean shouldRender(boolean isRendering) { return true; }

        private void initModel() {
            if (pawsModel == null) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                pawsModel = new PawsModel(modelSet.bakeLayer(PawsModel.LAYER_LOCATION));
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public <M extends LivingEntity> void render(
                ItemStack stack, SlotReference reference, PoseStack matrices,
                EntityModel<M> model, MultiBufferSource buffers,
                int light, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(stack.getItem() instanceof DyeableEquipmentItem d)) return;
            initModel();

            float[] mc = EquipmentColorRenderer.unpack(d.getMainColor(stack));
            float[] ac = EquipmentColorRenderer.unpack(d.getAccentColor(stack));
            var l1 = d.getEntityLayer1();
            var l2 = d.getEntityLayer2();
            boolean slim = model instanceof PlayerModel<?> pm && pm.slim;

            matrices.pushPose();
            ((HumanoidModel<?>) model).rightArm.translateAndRotate(matrices);
            matrices.translate(0f, -0.1f, 0f);
            matrices.scale(1.2f, 1.2f, 1.2f);
            if (slim) {
                pawsModel.renderPawsSlim(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPawsSlim(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            } else {
                pawsModel.renderPaws(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPaws(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            }
            matrices.popPose();

            matrices.pushPose();
            ((HumanoidModel<?>) model).leftArm.translateAndRotate(matrices);
            matrices.translate(0f, -0.1f, 0f);
            matrices.scale(-1.2f, 1.2f, 1.2f);
            if (slim) {
                pawsModel.renderPawsSlim(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPawsSlim(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            } else {
                pawsModel.renderPaws(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPaws(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            }
            matrices.popPose();
        }
    }

    private static class ToeBeansAccessoryRenderer implements AccessoryRenderer {
        private ToeBeansModel toeBeansModel;

        @Override public boolean shouldRender(boolean isRendering) { return true; }

        private void initModel() {
            if (toeBeansModel == null) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                toeBeansModel = new ToeBeansModel(modelSet.bakeLayer(ToeBeansModel.LAYER_LOCATION));
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public <M extends LivingEntity> void render(
                ItemStack stack, SlotReference reference, PoseStack matrices,
                EntityModel<M> model, MultiBufferSource buffers,
                int light, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(stack.getItem() instanceof DyeableEquipmentItem d)) return;
            initModel();

            float[] mc = EquipmentColorRenderer.unpack(d.getMainColor(stack));
            float[] ac = EquipmentColorRenderer.unpack(d.getAccentColor(stack));
            var l1 = d.getEntityLayer1();
            var l2 = d.getEntityLayer2();

            matrices.pushPose();
            ((HumanoidModel<?>) model).rightLeg.translateAndRotate(matrices);
            matrices.scale(1.26f, 1.26f, 1.26f);
            toeBeansModel.renderToeBeansThick(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
            toeBeansModel.renderToeBeansThick(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            matrices.popPose();

            matrices.pushPose();
            ((HumanoidModel<?>) model).leftLeg.translateAndRotate(matrices);
            matrices.scale(-1.26f, 1.26f, 1.26f);
            toeBeansModel.renderToeBeansThick(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
            toeBeansModel.renderToeBeansThick(matrices, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            matrices.popPose();
        }
    }
}
