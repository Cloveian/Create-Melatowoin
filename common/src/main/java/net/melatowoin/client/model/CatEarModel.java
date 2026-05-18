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
 * Cat ear model rendered as an armor helmet layer.
 * Geometry: layer size 32x32.
 */
@Environment(EnvType.CLIENT)
public class CatEarModel extends Model {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation("melatowoin", "cat_ears"), "main");

    private final ModelPart catear;

    public CatEarModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.catear = root.getChild("catear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 3D pixel-art ears converted from Blockbench java_block format.
        // Coordinate mapping: entity_x = block_x - 8, entity_y = -(block_to_y) - 8, entity_z = block_z - 8
        // Head top is entity Y=-8; ear tips reach Y=-13.
        // Left ear (block X 2-7) → entity X -6 to -1
        // Right ear (block X 9-14) → entity X 1 to 6
        // UV layout fits in 16×16 (box UV, both ears share the same UV region).
        root.addOrReplaceChild("catear",
                CubeListBuilder.create()
                        // === LEFT EAR ===
                        // base strip
                        .texOffs(0, 0).addBox(-6, -9, -2, 5, 1, 1)
                        // upper-row bar
                        .texOffs(0, 2).addBox(-5, -12, -3, 3, 1, 1)
                        // tall centre block
                        .texOffs(0, 4).addBox(-5, -11, -2, 3, 2, 1)
                        // left-side pillar
                        .texOffs(0, 7).addBox(-6, -10, -3, 1, 2, 1)
                        // right-side pillar
                        .texOffs(4, 7).addBox(-2, -10, -3, 1, 2, 1)
                        // top-centre cube
                        .texOffs(0, 10).addBox(-4, -12, -2, 1, 1, 1)
                        // tip cube
                        .texOffs(4, 10).addBox(-4, -13, -3, 1, 1, 1)
                        // inner-left accent
                        .texOffs(8, 10).addBox(-5, -11, -3, 1, 1, 1)
                        // inner-right accent
                        .texOffs(12, 10).addBox(-3, -11, -3, 1, 1, 1)
                        // === RIGHT EAR ===
                        .texOffs(0, 0).addBox(1, -9, -2, 5, 1, 1)
                        .texOffs(0, 2).addBox(2, -12, -3, 3, 1, 1)
                        .texOffs(0, 4).addBox(2, -11, -2, 3, 2, 1)
                        .texOffs(0, 7).addBox(1, -10, -3, 1, 2, 1)
                        .texOffs(4, 7).addBox(5, -10, -3, 1, 2, 1)
                        .texOffs(0, 10).addBox(3, -12, -2, 1, 1, 1)
                        .texOffs(4, 10).addBox(3, -13, -3, 1, 1, 1)
                        .texOffs(8, 10).addBox(2, -11, -3, 1, 1, 1)
                        .texOffs(12, 10).addBox(4, -11, -3, 1, 1, 1),
                PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        catear.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    /** Convenience: render with default color (white, full alpha). */
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay) {
        renderToBuffer(poseStack, buffer, packedLight, packedOverlay, 1f, 1f, 1f, 1f);
    }
}
