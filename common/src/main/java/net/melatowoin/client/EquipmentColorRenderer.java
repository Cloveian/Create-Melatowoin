package net.melatowoin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * Shared utility for two-pass tinted rendering of dyeable cat equipment.
 *
 * Pass 1: layer1 texture tinted with the main (fur) color.
 * Pass 2: layer2 texture tinted with the accent (paw pad / inner ear) color.
 *
 * Both textures are grayscale. layer1 has accent regions transparent; layer2
 * has main regions transparent. entityCutoutNoCull composites them cleanly.
 */
@Environment(EnvType.CLIENT)
public final class EquipmentColorRenderer {

    private EquipmentColorRenderer() {}

    /**
     * Render {@code model} in two tinted passes.
     *
     * @param pose        current PoseStack (caller is responsible for push/pop)
     * @param buffers     buffer source
     * @param model       the geometry to render
     * @param layer1      path to the main-color layer texture
     * @param layer2      path to the accent-color layer texture
     * @param mainColor   packed RGB int for the main tint
     * @param accentColor packed RGB int for the accent tint
     * @param light       packed light value
     */
    public static void renderTwoPass(
            PoseStack pose, MultiBufferSource buffers, Model model,
            ResourceLocation layer1, ResourceLocation layer2,
            int mainColor, int accentColor, int light) {

        float[] mc = unpack(mainColor);
        model.renderToBuffer(pose,
                buffers.getBuffer(RenderType.entityCutoutNoCull(layer1)),
                light, OverlayTexture.NO_OVERLAY,
                mc[0], mc[1], mc[2], 1f);

        float[] ac = unpack(accentColor);
        model.renderToBuffer(pose,
                buffers.getBuffer(RenderType.entityCutoutNoCull(layer2)),
                light, OverlayTexture.NO_OVERLAY,
                ac[0], ac[1], ac[2], 1f);
    }

    /** Unpack a packed 0xRRGGBB int into normalized [0, 1] floats: {r, g, b}. */
    public static float[] unpack(int color) {
        return new float[]{
            ((color >> 16) & 0xFF) / 255f,
            ((color >>  8) & 0xFF) / 255f,
            ( color        & 0xFF) / 255f
        };
    }
}
