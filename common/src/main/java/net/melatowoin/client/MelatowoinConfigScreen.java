package net.melatowoin.client;

import net.melatowoin.MelatowoinConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/** Config screen for Create: MelatOwOin. One slider, a few toggles. */
public class MelatowoinConfigScreen extends Screen {

    private final Screen parent;
    private double  reduction;
    private boolean earsLift;
    private boolean hideHelmet;
    private boolean hideChestplate;

    public MelatowoinConfigScreen(Screen parent) {
        super(Component.literal("Create: MelatOwOin"));
        this.parent          = parent;
        this.reduction       = MelatowoinConfig.getFullSetSoundReduction();
        this.earsLift        = MelatowoinConfig.getEarsLiftUnderHelmet();
        this.hideHelmet      = MelatowoinConfig.getHideHelmetWithCatEars();
        this.hideChestplate  = MelatowoinConfig.getHideChestplateWithPaws();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 4 + 24;
        final int W = 220;
        final int H = 20;
        final int GAP = 4;

        // Slider
        addRenderableWidget(new ReductionSlider(centerX - W / 2, y, W, H, reduction));
        y += H + GAP;

        // Toggles
        addRenderableWidget(toggle(centerX - W / 2, y, W, H,
                "Lift ears 1px under helmet",
                () -> earsLift,
                v -> earsLift = v));
        y += H + GAP;

        addRenderableWidget(toggle(centerX - W / 2, y, W, H,
                "Hide helmet when wearing cat ears",
                () -> hideHelmet,
                v -> hideHelmet = v));
        y += H + GAP;

        addRenderableWidget(toggle(centerX - W / 2, y, W, H,
                "Hide chestplate when wearing paws",
                () -> hideChestplate,
                v -> hideChestplate = v));

        // Done button
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> commitAndClose())
                .bounds(centerX - 100, this.height - 28, 200, 20).build());
    }

    private Button toggle(int x, int y, int w, int h, String label,
                          java.util.function.BooleanSupplier getter,
                          java.util.function.Consumer<Boolean> setter) {
        Button[] holder = new Button[1];
        Button b = Button.builder(
                Component.literal(label + ": " + (getter.getAsBoolean() ? "ON" : "OFF")),
                btn -> {
                    boolean next = !getter.getAsBoolean();
                    setter.accept(next);
                    holder[0].setMessage(Component.literal(label + ": " + (next ? "ON" : "OFF")));
                }).bounds(x, y, w, h).build();
        holder[0] = b;
        return b;
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        gfx.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        gfx.drawCenteredString(this.font,
                Component.literal("Lower the slider to hear your own sounds through the cat outfit."),
                this.width / 2, this.height / 4 + 4, 0xAAAAAA);
        super.render(gfx, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        commitAndClose();
    }

    private void commitAndClose() {
        MelatowoinConfig.setFullSetSoundReduction(reduction);
        MelatowoinConfig.setEarsLiftUnderHelmet(earsLift);
        MelatowoinConfig.setHideHelmetWithCatEars(hideHelmet);
        MelatowoinConfig.setHideChestplateWithPaws(hideChestplate);
        // Push the three render prefs up so every other client renders us
        // according to our choices, not theirs.
        MelatowoinConfig.sendToServer();
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
