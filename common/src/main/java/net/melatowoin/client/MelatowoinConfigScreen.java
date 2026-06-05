package net.melatowoin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melatowoin.MelatowoinConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/** Tiny config screen with one slider for the full-set sound reduction (0–100%). */
public class MelatowoinConfigScreen extends Screen {

    private final Screen parent;
    private double reduction;

    public MelatowoinConfigScreen(Screen parent) {
        super(Component.literal("Create: MelatOwOin"));
        this.parent = parent;
        this.reduction = MelatowoinConfig.getFullSetSoundReduction();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int sliderY = this.height / 4 + 24;

        addRenderableWidget(new ReductionSlider(centerX - 100, sliderY, 200, 20, reduction));

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> {
            MelatowoinConfig.setFullSetSoundReduction(reduction);
            if (minecraft != null) minecraft.setScreen(parent);
        }).bounds(centerX - 100, this.height - 28, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        gfx.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        gfx.drawCenteredString(this.font,
                Component.literal("Lower to let your own sounds through while wearing the full cat outfit."),
                this.width / 2, this.height / 4 + 4, 0xAAAAAA);
        super.render(gfx, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        MelatowoinConfig.setFullSetSoundReduction(reduction);
        if (minecraft != null) minecraft.setScreen(parent);
    }

    private class ReductionSlider extends AbstractSliderButton {
        ReductionSlider(int x, int y, int width, int height, double initial) {
            super(x, y, width, height, Component.empty(), initial);
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int pct = (int) Math.round(this.value * 100);
            this.setMessage(Component.literal("Full Set Sound Reduction: " + pct + "%"));
        }

        @Override
        protected void applyValue() {
            MelatowoinConfigScreen.this.reduction = this.value;
        }
    }
}
