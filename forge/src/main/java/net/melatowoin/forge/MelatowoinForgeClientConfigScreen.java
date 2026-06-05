package net.melatowoin.forge;

import net.melatowoin.client.MelatowoinConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * Registers a Forge config-screen factory so the mod's settings can be opened
 * from the "Mods" menu. Isolated into its own class so the client-only Screen
 * reference is only loaded on the client side.
 */
public final class MelatowoinForgeClientConfigScreen {
    private MelatowoinForgeClientConfigScreen() {}

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (minecraft, parent) -> new MelatowoinConfigScreen(parent)));
    }
}
