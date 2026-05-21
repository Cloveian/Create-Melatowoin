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

/**
 * Toe-bean boot overlay rendered on both player legs (TOE_BEANS / boots slot).
 *
 * Rendered after contextModel.rightLeg / leftLeg .translateAndRotate(). The
 * vanilla leg box occupies (-2, 0, -2) → (2, 12, 2) relative to the leg pivot;
 * the boot covers the lower third (y = 9 → 12) plus a small toe overhang.
 *
 * Layer size 16×16 — paint toe_beans_layer1.png (boot exterior, main color) and
 * toe_beans_layer2.png (toe bean pads on front face, accent color, alpha-masked).
 */
@Environment(EnvType.CLIENT)
public class ToeBeansModel extends Model {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation("melatowoin", "toe_beans"), "main");

    private final ModelPart toeBeansGroup;
    private final ModelPart toeBeansSlimGroup;

    public ToeBeansModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.toeBeansGroup     = root.getChild("toe_beans_group");
        this.toeBeansSlimGroup = root.getChild("toe_beans_slim_group");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition toeBeansGroup = root.addOrReplaceChild("toe_beans_group",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0, 8.01f, 0, 0, 0, (float)(Math.PI)));

        toeBeansGroup.addOrReplaceChild("toe_beans_print", CubeListBuilder.create()
                .texOffs(0, 8).addBox(1.8F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 8).addBox(1.0F, -0.5F, -1.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 20).addBox(0.6F, -0.5F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(16, 8).addBox(-0.2F, -0.5F, -1.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(16, 17).addBox(1.4F, -0.5F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 11).addBox(1.0F, -0.5F, -0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 20).addBox(1.0F, -0.5F, 0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 14).addBox(0.6F, -0.5F, -0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 14).addBox(0.2F, -0.5F, -0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(16, 14).addBox(-0.2F, -0.5F, -0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 17).addBox(-0.6F, -0.5F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 17).addBox(-0.2F, -0.5F, 0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 11).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F)),
                PartPose.offset(-1.4F, -2.0F, -0.4F));

        toeBeansGroup.addOrReplaceChild("toe_beans_bb_main",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition toeBeansSlimGroup = root.addOrReplaceChild("toe_beans_slim_group",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1, 8.01f, 0, 0, 0, (float)(Math.PI)));

        toeBeansSlimGroup.addOrReplaceChild("toe_beans_slim_print", CubeListBuilder.create()
                .texOffs(0, 8).addBox(1.8F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 8).addBox(1.0F, -0.5F, -1.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 20).addBox(0.6F, -0.5F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(16, 8).addBox(-0.2F, -0.5F, -1.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(16, 17).addBox(1.4F, -0.5F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 11).addBox(1.0F, -0.5F, -0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 20).addBox(1.0F, -0.5F, 0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 14).addBox(0.6F, -0.5F, -0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 14).addBox(0.2F, -0.5F, -0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(16, 14).addBox(-0.2F, -0.5F, -0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 17).addBox(-0.6F, -0.5F, 0.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(8, 17).addBox(-0.2F, -0.5F, 0.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 11).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F)),
                PartPose.offset(-1.4F, -2.0F, -0.4F));

        toeBeansSlimGroup.addOrReplaceChild("toe_beans_slim_bb_main",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    public void renderToeBeansThick(PoseStack poseStack, VertexConsumer buffer,
                                    int packedLight, int packedOverlay,
                                    float red, float green, float blue, float alpha) {
        toeBeansGroup.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderToeBeansSlim(PoseStack poseStack, VertexConsumer buffer,
                                   int packedLight, int packedOverlay,
                                   float red, float green, float blue, float alpha) {
        toeBeansSlimGroup.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        renderToeBeansThick(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
