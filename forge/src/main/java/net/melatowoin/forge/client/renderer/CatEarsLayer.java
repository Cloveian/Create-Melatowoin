package net.melatowoin.forge.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melatowoin.client.EquipmentColorRenderer;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.PawsModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.model.ToeBeansModel;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Forge render layer that draws all four cat equipment pieces on players:
 *   HEAD  → cat ears  (CatEarModel, two-pass tinted)
 *   CHEST → paws      (PawsModel on both arms, two-pass tinted)
 *   LEGS  → cat tail  (TailModel, two-pass tinted + animated)
 *   FEET  → toe beans (ToeBeansModel on both legs, two-pass tinted)
 *
 * Registered for both skin types ("default" / "slim") in MelatowoinForgeClient.
 * Vanilla HumanoidArmorLayer is suppressed via getArmorTexture → transparent.png.
 */
public class CatEarsLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private CatEarModel  earModel;
    private TailModel    tailModel;
    private PawsModel    pawsModel;
    private ToeBeansModel toeBeansModel;

    public CatEarsLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    private void initModels() {
        if (earModel != null) return;
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        earModel      = new CatEarModel(modelSet.bakeLayer(CatEarModel.LAYER_LOCATION));
        tailModel     = new TailModel(modelSet.bakeLayer(TailModel.LAYER_LOCATION));
        pawsModel     = new PawsModel(modelSet.bakeLayer(PawsModel.LAYER_LOCATION));
        toeBeansModel = new ToeBeansModel(modelSet.bakeLayer(ToeBeansModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        initModels();
        PlayerModel<AbstractClientPlayer> body = getParentModel();

        // ── HEAD: cat ears ──
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (head.getItem() instanceof DyeableEquipmentItem d
                && d.getEquipType() == DyeableEquipmentItem.EquipType.CAT_EARS) {
            pose.pushPose();
            body.head.translateAndRotate(pose);
            final float earScale = 8f / 12f;
            final float headTop  = 8f / 16f;
            pose.translate(0.0, -headTop, 0.0);
            pose.scale(earScale, earScale, earScale);
            pose.translate(0.0, headTop, 0.0);
            EquipmentColorRenderer.renderTwoPass(pose, buffers, earModel,
                    d.getEntityLayer1(), d.getEntityLayer2(),
                    d.getMainColor(head), d.getAccentColor(head), light);
            pose.popPose();
        }

        // ── CHEST: paws on both arms ──
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof DyeableEquipmentItem d
                && d.getEquipType() == DyeableEquipmentItem.EquipType.PAWS) {
            float[] mc = EquipmentColorRenderer.unpack(d.getMainColor(chest));
            float[] ac = EquipmentColorRenderer.unpack(d.getAccentColor(chest));
            var l1 = d.getEntityLayer1();
            var l2 = d.getEntityLayer2();
            boolean slim = body.slim;

            pose.pushPose();
            body.rightArm.translateAndRotate(pose);
            pose.translate(0f, -0.1f, 0f);
            pose.scale(1.2f, 1.2f, 1.2f);
            if (slim) {
                pawsModel.renderPawsSlim(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPawsSlim(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            } else {
                pawsModel.renderPaws(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPaws(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            }
            pose.popPose();

            pose.pushPose();
            body.leftArm.translateAndRotate(pose);
            pose.translate(0f, -0.1f, 0f);
            pose.scale(-1.2f, 1.2f, 1.2f);
            if (slim) {
                pawsModel.renderPawsSlim(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPawsSlim(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            } else {
                pawsModel.renderPaws(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
                pawsModel.renderPaws(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            }
            pose.popPose();
        }

        // ── LEGS: cat tail ──
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof DyeableEquipmentItem d
                && d.getEquipType() == DyeableEquipmentItem.EquipType.TAIL) {
            pose.pushPose();
            body.body.translateAndRotate(pose);
            tailModel.setupAnim(player, partialTick);
            EquipmentColorRenderer.renderTwoPass(pose, buffers, tailModel,
                    d.getEntityLayer1(), d.getEntityLayer2(),
                    d.getMainColor(legs), d.getAccentColor(legs), light);
            pose.popPose();
        }

        // ── FEET: toe beans on both legs ──
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        if (feet.getItem() instanceof DyeableEquipmentItem d
                && d.getEquipType() == DyeableEquipmentItem.EquipType.TOE_BEANS) {
            float[] mc = EquipmentColorRenderer.unpack(d.getMainColor(feet));
            float[] ac = EquipmentColorRenderer.unpack(d.getAccentColor(feet));
            var l1 = d.getEntityLayer1();
            var l2 = d.getEntityLayer2();

            pose.pushPose();
            body.rightLeg.translateAndRotate(pose);
            pose.scale(1.26f, 1.26f, 1.26f);
            toeBeansModel.renderToeBeansThick(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
            toeBeansModel.renderToeBeansThick(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            pose.popPose();

            pose.pushPose();
            body.leftLeg.translateAndRotate(pose);
            pose.scale(-1.26f, 1.26f, 1.26f);
            toeBeansModel.renderToeBeansThick(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, mc[0], mc[1], mc[2], 1f);
            toeBeansModel.renderToeBeansThick(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, ac[0], ac[1], ac[2], 1f);
            pose.popPose();
        }
    }
}
