package net.melatowoin.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.melatowoin.registry.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * A full-screen dark overlay shown while the player is under the Eepy effect.
 * The screen cannot be dismissed by pressing Escape while the effect is active.
 */
@Environment(EnvType.CLIENT)
public class EepyScreen extends Screen {

    private static EepyScreen INSTANCE = null;

    public EepyScreen() {
        super(Component.literal("zzz..."));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Dark overlay covering the entire screen
        graphics.fill(0, 0, this.width, this.height, 0xDD000000);
        // "zzz..." text centered
        int textX = (this.width - this.font.width("zzz...")) / 2;
        int textY = this.height / 2 - 10;
        graphics.drawString(this.font, "zzz...", textX, textY, 0xAAAAAA, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    /**
     * Prevent closing with Escape while the Eepy effect is still active.
     */
    @Override
    public boolean shouldCloseOnEsc() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.hasEffect(ModEffects.EEPY.get())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ---- Static helpers called from network receive / tick ----

    public static void open() {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof EepyScreen)) {
            INSTANCE = new EepyScreen();
            mc.setScreen(INSTANCE);
        }
    }

    public static void close() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof EepyScreen) {
            mc.setScreen(null);
            INSTANCE = null;
        }
    }

    /**
     * Called each client tick while the Eepy effect is active to ensure
     * the screen stays open if something else (e.g. opening inventory) dismisses it.
     */
    public static void onClientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.hasEffect(ModEffects.EEPY.get())) {
            if (!(mc.screen instanceof EepyScreen)) {
                open();
            }
        }
    }
}
