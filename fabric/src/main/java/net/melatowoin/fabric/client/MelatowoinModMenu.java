package net.melatowoin.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.melatowoin.client.MelatowoinConfigScreen;

public class MelatowoinModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MelatowoinConfigScreen::new;
    }
}
