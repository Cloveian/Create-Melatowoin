package net.melatowoin.forge.client;

import net.melatowoin.MelatowoinMod;
import net.melatowoin.client.screen.EepyScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Game-bus client event subscriber: keeps the Eepy screen open each tick.
 */
@Mod.EventBusSubscriber(modid = MelatowoinMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientTickHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EepyScreen.onClientTick();
        }
    }
}
