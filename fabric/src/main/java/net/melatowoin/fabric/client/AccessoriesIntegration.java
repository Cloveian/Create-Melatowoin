package net.melatowoin.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import io.wispforest.accessories.api.client.AccessoryRenderer;
import io.wispforest.accessories.api.slot.SlotReference;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.item.CatEarsArmorItem;
import net.melatowoin.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AccessoriesIntegration {

    public static void registerRenderers() {
        Item[] helmets = {
            ModItems.BLACKCATEARS.get(),
            ModItems.BLUECATEARS.get(),
            ModItems.BROWNCATEARS.get(),
            ModItems.CYANCATEARS.get(),
            ModItems.GRAYCATEARS.get(),
            ModItems.GREENCATEARS.get(),
            ModItems.LIGHTBLUECATEARS.get(),
            ModItems.LIGHTGRAYCATEARS.get(),
            ModItems.LIMECATEARS.get(),
            ModItems.MAGENTACATEARS.get(),
            ModItems.ORANGECATEARS.get(),
            ModItems.PINKCATEARS.get(),
            ModItems.PURPLECATEARS.get(),
            ModItems.REDCATEARS.get(),
            ModItems.WHITECATEARS.get(),
            ModItems.YELLOWCATEARS.get(),
        };

        Item[] leggings = {
            ModItems.BLACKTAIL.get(),
            ModItems.BLUETAIL.get(),
            ModItems.BROWNTAIL.get(),
            ModItems.CYANTAIL.get(),
            ModItems.GRAYTAIL.get(),
            ModItems.GREENTAIL.get(),
            ModItems.LIGHTBLUETAIL.get(),
            ModItems.LIGHTGRAYTAIL.get(),
            ModItems.LIMETAIL.get(),
            ModItems.MAGENTATAIL.get(),
            ModItems.ORANGETAIL.get(),
            ModItems.PINKTAIL.get(),
            ModItems.PURPLETAIL.get(),
            ModItems.REDTAIL.get(),
            ModItems.WHITETAIL.get(),
            ModItems.YELLOWTAIL.get(),
        };

        for (Item item : helmets) {
            AccessoriesRendererRegistry.registerRenderer(item, EarAccessoryRenderer::new);
        }
        for (Item item : leggings) {
            AccessoriesRendererRegistry.registerRenderer(item, TailAccessoryRenderer::new);
        }
    }

    private static class EarAccessoryRenderer implements AccessoryRenderer {
        private CatEarModel earModel;

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
                EntityModel<M> model, MultiBufferSource multiBufferSource,
                int light, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(stack.getItem() instanceof CatEarsArmorItem armorItem)) return;
            initModel();

            matrices.pushPose();
            ((HumanoidModel<?>) model).head.translateAndRotate(matrices);

            final float earScale = 8f / 12f;
            final float headTop  = 8f / 16f;
            matrices.translate(0.0, -headTop, 0.0);
            matrices.scale(earScale, earScale, earScale);
            matrices.translate(0.0, headTop, 0.0);

            var buffer = multiBufferSource.getBuffer(
                    RenderType.entityCutoutNoCull(new ResourceLocation(armorItem.getEarTexture())));
            earModel.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY);
            matrices.popPose();
        }
    }

    private static class TailAccessoryRenderer implements AccessoryRenderer {
        private TailModel tailModel;

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
                EntityModel<M> model, MultiBufferSource multiBufferSource,
                int light, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!(stack.getItem() instanceof CatEarsArmorItem armorItem)) return;
            initModel();

            matrices.pushPose();
            ((HumanoidModel<?>) model).body.translateAndRotate(matrices);
            tailModel.setupAnim(reference.entity(), partialTicks);

            var buffer = multiBufferSource.getBuffer(
                    RenderType.entityCutoutNoCull(new ResourceLocation(armorItem.getTailTexture())));
            tailModel.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY);
            matrices.popPose();
        }
    }
}
