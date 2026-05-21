// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class paws_slim<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "paws_slim"), "main");
	private final ModelPart print;
	private final ModelPart bb_main;

	public paws_slim(ModelPart root) {
		this.print = root.getChild("print");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition print = partdefinition.addOrReplaceChild("print", CubeListBuilder.create().texOffs(0, 8).addBox(1.8F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F))
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
		.texOffs(0, 11).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.6F)), PartPose.offset(-1.4F, 22.0F, -0.4F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		print.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}