package net.melatowoin.forge.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Forge render layer that draws cat ears (HEAD slot) and cat tail (LEGS slot)
 * on players wearing DyeableEquipmentItem pieces.
 *
 * Registered for both skin types ("default" / "slim") in MelatowoinForgeClient.
 * Mirrors the logic in FabricCatEarsRenderer.
 */
public class CatEarsLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private CatEarModel earModel;
    private TailModel tailModel;

    public CatEarsLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    private void initModels() {
        if (earModel == null || tailModel == null) {
            EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
            earModel = new CatEarModel(modelSet.bakeLayer(CatEarModel.LAYER_LOCATION));
            tailModel = new TailModel(modelSet.bakeLayer(TailModel.LAYER_LOCATION));
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        initModels();

        // HEAD slot — cat ears
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.getItem() instanceof DyeableEquipmentItem armorItem
                && armorItem.getEquipType() == DyeableEquipmentItem.EquipType.CAT_EARS) {
            poseStack.pushPose();
            this.getParentModel().head.translateAndRotate(poseStack);
            final float earScale = 8f / 12f;
            final float headTop  = 8f / 16f;
            poseStack.translate(0.0, -headTop, 0.0);
            poseStack.scale(earScale, earScale, earScale);
            poseStack.translate(0.0, headTop, 0.0);
            var buffer = bufferSource.getBuffer(
                    RenderType.entityCutoutNoCull(new ResourceLocation(armorItem.getEarTexture())));
            earModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }

        // LEGS slot — cat tail
        ItemStack legsItem = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legsItem.getItem() instanceof DyeableEquipmentItem armorItem
                && armorItem.getEquipType() == DyeableEquipmentItem.EquipType.TAIL) {
            poseStack.pushPose();
            this.getParentModel().body.translateAndRotate(poseStack);
            tailModel.setupAnim(player, partialTick);
            var buffer = bufferSource.getBuffer(
                    RenderType.entityCutoutNoCull(new ResourceLocation(armorItem.getTailTexture())));
            tailModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}
