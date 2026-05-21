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

@Environment(EnvType.CLIENT)
public class PawsModel extends Model {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation("melatowoin", "paws"), "main");

    private final ModelPart pawsGroup;
    private final ModelPart pawsSlimGroup;

    public PawsModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.pawsGroup     = root.getChild("paws_group");
        this.pawsSlimGroup = root.getChild("paws_slim_group");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Offset -24 in Y so that Blockbench (0,0,0) — the paw center — sits at the
        // render attachment point (wherever translateAndRotate places the origin).
        PartDefinition pawsGroup = root.addOrReplaceChild("paws_group",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0, 8, 0, -(float)(Math.PI / 2), -(float)(Math.PI / 2), (float)(Math.PI)));

        pawsGroup.addOrReplaceChild("paws_print", CubeListBuilder.create()
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

        pawsGroup.addOrReplaceChild("paws_bb_main",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pawsSlimGroup = root.addOrReplaceChild("paws_slim_group",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1, 8, 0, -(float)(Math.PI / 2), -(float)(Math.PI / 2), (float)(Math.PI)));

        pawsSlimGroup.addOrReplaceChild("paws_slim_print", CubeListBuilder.create()
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

        pawsSlimGroup.addOrReplaceChild("paws_slim_bb_main",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    public void renderPaws(PoseStack pose, VertexConsumer buf,
            int light, int overlay, float r, float g, float b, float a) {
        pawsGroup.render(pose, buf, light, overlay, r, g, b, a);
    }

    public void renderPawsSlim(PoseStack pose, VertexConsumer buf,
            int light, int overlay, float r, float g, float b, float a) {
        pawsSlimGroup.render(pose, buf, light, overlay, r, g, b, a);
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer buf,
            int light, int overlay, float r, float g, float b, float a) {
        renderPaws(pose, buf, light, overlay, r, g, b, a);
    }
}
