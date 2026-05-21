package net.melatowoin.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.melatowoin.client.EquipmentColorRenderer;
import net.melatowoin.client.model.PawsModel;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Renders cat paws in first-person view.
 *
 * Injects after renderRightHand / renderLeftHand returns, while the PoseStack is
 * still in the arm-local coordinate space established by renderPlayerArm.
 * We replicate the arm pivot via armPart.translateAndRotate() (rotation is 0
 * since renderHand() resets it) to position the paw geometry correctly.
 */
@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    private PawsModel melatowoin$pawsModel;

    private void melatowoin$initPawsModel() {
        if (melatowoin$pawsModel != null) return;
        melatowoin$pawsModel = new PawsModel(
                Minecraft.getInstance().getEntityModels().bakeLayer(PawsModel.LAYER_LOCATION));
    }

    @Inject(method = "renderPlayerArm",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;" +
                             "renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;" +
                             "Lnet/minecraft/client/renderer/MultiBufferSource;" +
                             "ILnet/minecraft/client/player/AbstractClientPlayer;)V",
                    shift = At.Shift.BEFORE))
    private void melatowoin$beforeRenderRightArm(PoseStack poseStack, MultiBufferSource buffer,
                                                  int light, float equip, float swing,
                                                  HumanoidArm side, CallbackInfo ci) {
        melatowoin$rotateArmIfPaws(poseStack, HumanoidArm.RIGHT);
    }

    @Inject(method = "renderPlayerArm",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;" +
                             "renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;" +
                             "Lnet/minecraft/client/renderer/MultiBufferSource;" +
                             "ILnet/minecraft/client/player/AbstractClientPlayer;)V",
                    shift = At.Shift.BEFORE))
    private void melatowoin$beforeRenderLeftArm(PoseStack poseStack, MultiBufferSource buffer,
                                                 int light, float equip, float swing,
                                                 HumanoidArm side, CallbackInfo ci) {
        melatowoin$rotateArmIfPaws(poseStack, HumanoidArm.LEFT);
    }

    @Inject(method = "renderPlayerArm",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;" +
                             "renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;" +
                             "Lnet/minecraft/client/renderer/MultiBufferSource;" +
                             "ILnet/minecraft/client/player/AbstractClientPlayer;)V",
                    shift = At.Shift.AFTER))
    private void melatowoin$afterRenderRightArm(PoseStack poseStack, MultiBufferSource buffer,
                                                 int light, float equip, float swing,
                                                 HumanoidArm side, CallbackInfo ci) {
        melatowoin$renderFirstPersonPaw(poseStack, buffer, light, HumanoidArm.RIGHT);
    }

    @Inject(method = "renderPlayerArm",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;" +
                             "renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;" +
                             "Lnet/minecraft/client/renderer/MultiBufferSource;" +
                             "ILnet/minecraft/client/player/AbstractClientPlayer;)V",
                    shift = At.Shift.AFTER))
    private void melatowoin$afterRenderLeftArm(PoseStack poseStack, MultiBufferSource buffer,
                                                int light, float equip, float swing,
                                                HumanoidArm side, CallbackInfo ci) {
        melatowoin$renderFirstPersonPaw(poseStack, buffer, light, HumanoidArm.LEFT);
    }

    private static ItemStack melatowoin$findPaws(Minecraft mc) {
        if (mc.player == null) return ItemStack.EMPTY;
        ItemStack chest = mc.player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof DyeableEquipmentItem d
                && d.getEquipType() == DyeableEquipmentItem.EquipType.PAWS) return chest;
        return AccessoriesSlotHelper.findPawsInAccessories.apply(mc.player);
    }

    private void melatowoin$rotateArmIfPaws(PoseStack poseStack, HumanoidArm side) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = melatowoin$findPaws(mc);
        if (stack.isEmpty()) return;
        poseStack.translate(0f, 0.3f, 0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
    }

    private void melatowoin$renderFirstPersonPaw(PoseStack poseStack, MultiBufferSource buffer,
                                                  int light, HumanoidArm side) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = melatowoin$findPaws(mc);
        if (stack.isEmpty()) return;
        DyeableEquipmentItem d = (DyeableEquipmentItem) stack.getItem();

        melatowoin$initPawsModel();

        PlayerRenderer pr = (PlayerRenderer) entityRenderDispatcher.getRenderer(mc.player);
        PlayerModel<AbstractClientPlayer> playerModel = pr.getModel();
        boolean slim = playerModel.slim;
        // Replicate the arm pivot: renderHand() resets xRot to 0, so translateAndRotate
        // only applies the arm's initialPose offset ((-5,2,0) right / (5,2,0) left).
        ModelPart armPart = (side == HumanoidArm.RIGHT) ? playerModel.rightArm : playerModel.leftArm;

        float[] main   = EquipmentColorRenderer.unpack(d.getMainColor(stack));
        float[] accent = EquipmentColorRenderer.unpack(d.getAccentColor(stack));
        var l1 = d.getEntityLayer1();
        var l2 = d.getEntityLayer2();

        poseStack.pushPose();
        armPart.translateAndRotate(poseStack);
        poseStack.translate(0f, -0.1f, 0f);
        poseStack.scale(1.2f, 1.2f, 1.2f);
        if (side == HumanoidArm.RIGHT) {
            if (slim) {
                melatowoin$pawsModel.renderPawsSlim(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, main[0],   main[1],   main[2],   1f);
                melatowoin$pawsModel.renderPawsSlim(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, accent[0], accent[1], accent[2], 1f);
            } else {
                melatowoin$pawsModel.renderPaws(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, main[0],   main[1],   main[2],   1f);
                melatowoin$pawsModel.renderPaws(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, accent[0], accent[1], accent[2], 1f);
            }
        } else {
            poseStack.scale(-1f, 1f, 1f);
            if (slim) {
                melatowoin$pawsModel.renderPawsSlim(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, main[0],   main[1],   main[2],   1f);
                melatowoin$pawsModel.renderPawsSlim(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, accent[0], accent[1], accent[2], 1f);
            } else {
                melatowoin$pawsModel.renderPaws(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l1)), light, OverlayTexture.NO_OVERLAY, main[0],   main[1],   main[2],   1f);
                melatowoin$pawsModel.renderPaws(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(l2)), light, OverlayTexture.NO_OVERLAY, accent[0], accent[1], accent[2], 1f);
            }
        }
        poseStack.popPose();
    }
}
