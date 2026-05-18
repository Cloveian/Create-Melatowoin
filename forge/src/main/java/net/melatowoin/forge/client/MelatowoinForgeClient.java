package net.melatowoin.forge.client;

import dev.architectury.networking.NetworkManager;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.screen.EepyScreen;
import net.melatowoin.forge.client.renderer.CatEarsLayer;
import net.melatowoin.network.EepyScreenPacket;
import net.melatowoin.registry.ModEntityTypes;
import net.minecraftforge.fml.ModList;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
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
        if (ModList.get().isLoaded("accessories")) {
            AccessoriesForgeIntegration.registerRenderers();
        }

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
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.CYAN_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ORANGE_PROJECTILE.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CatEarModel.LAYER_LOCATION, CatEarModel::createBodyLayer);
        event.registerLayerDefinition(TailModel.LAYER_LOCATION, TailModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new CatEarsLayer(renderer));
            }
        }
    }
}
