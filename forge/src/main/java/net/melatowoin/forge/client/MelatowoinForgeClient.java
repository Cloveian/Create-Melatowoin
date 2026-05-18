package net.melatowoin.forge.client;

import dev.architectury.networking.NetworkManager;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.screen.EepyScreen;
import net.melatowoin.network.EepyScreenPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Mod-bus client event subscriber: registers layer definitions and the eepy screen packet receiver.
 */
@Mod.EventBusSubscriber(modid = MelatowoinMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MelatowoinForgeClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Register Eepy screen network packet receiver (S2C)
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, EepyScreenPacket.ID,
                (buf, context) -> {
                    EepyScreenPacket packet = new EepyScreenPacket(buf);
                    context.queue(() -> {
                        if (packet.open) {
                            EepyScreen.open();
                        } else {
                            EepyScreen.close();
                        }
                    });
                });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CatEarModel.LAYER_LOCATION, CatEarModel::createBodyLayer);
        event.registerLayerDefinition(TailModel.LAYER_LOCATION, TailModel::createBodyLayer);
    }
}
