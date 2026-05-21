package net.melatowoin.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * Cat tail model rendered as an armor chestplate layer.
 * Geometry: layer size 32x32.
 */
@Environment(EnvType.CLIENT)
public class TailModel extends Model {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation("melatowoin", "cat_tail"), "main");

    private final ModelPart tail;

    // Smoothed rotation state — persists between frames so transitions are spring-like.
    private float smoothXRot = -0.35f;
    private float smoothYRot = 0.0f;

    public TailModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Tail pivot at the lower back of the body.
        // Rendered after contextModel.body.translateAndRotate():
        //   Y=0 = neck, Y=12 = hips, Z+ = behind the player.
        //
        // Two cubes share the same pivot and rotation, each covering a different
        // UV region of the 64×64 texture so EquipmentColorRenderer can tint them
        // independently in two passes:
        //   texOffs(0,  0): full-length body (14 deep) → tail_layer1.png (main color)
        //   texOffs(0, 18): shorter inner layer (12 deep, offset +2 in Z) → tail_layer2.png (accent color)
        root.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2, -2, -3, 4, 4, 14, new CubeDeformation(-0.3f))
                        .texOffs(0, 18)
                        .addBox(-2, -2, -1, 4, 4, 12, new CubeDeformation(-0.1f)),
                PartPose.offsetAndRotation(0, 11, 1, -0.35f, 0, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    /**
     * Animates the tail each frame.
     * Idles with a gentle side wag; wags faster and bobs when the entity is moving.
     */
    public void setupAnim(LivingEntity entity, float partialTick) {
        float time = (entity.tickCount + partialTick) * 0.05f;

        // Horizontal speed — clamped to [0, 1].
        float hSpeed = (float) Math.min(1.0, entity.getDeltaMovement().horizontalDistanceSqr() * 25.0);

        // Side-to-side wag: gentle idle + larger when moving.
        float targetYRot = Mth.sin(time * 2.0f) * 0.15f
                         + Mth.sin(time * 5.0f) * 0.35f * hSpeed;

        // Up/down target: walk bob on the ground, gentle oscillation while airborne.
        float targetXRot;
        if (entity.onGround()) {
            float walkBob = Mth.sin(time * 5.0f + Mth.HALF_PI) * 0.12f * hSpeed;
            targetXRot = -0.35f + walkBob;
        } else {
            // Tail wags back and forth in the air (simulates floating/bouncing).
            targetXRot = -0.35f + Mth.sin(time * 4.0f) * 0.35f;
        }

        // Spring-lerp toward target — carries current position across frames so
        // there are no sudden jumps when the entity leaves or touches the ground.
        smoothXRot += (targetXRot - smoothXRot) * 0.15f;
        smoothYRot += (targetYRot - smoothYRot) * 0.25f;

        tail.xRot = smoothXRot;
        tail.yRot = smoothYRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        tail.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    /** Convenience: render with default color (white, full alpha). */
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay) {
        renderToBuffer(poseStack, buffer, packedLight, packedOverlay, 1f, 1f, 1f, 1f);
    }
}
