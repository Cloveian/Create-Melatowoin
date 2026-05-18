package net.melatowoin.fabric.client;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.screen.EepyScreen;
import net.melatowoin.fabric.client.renderer.FabricCatEarsRenderer;
import net.melatowoin.network.EepyScreenPacket;
import net.melatowoin.registry.ModEntityTypes;
import net.melatowoin.registry.ModFluids;
import net.melatowoin.registry.ModItems;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class MelatowoinFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register model layers
        EntityModelLayerRegistry.registerModelLayer(CatEarModel.LAYER_LOCATION, CatEarModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(TailModel.LAYER_LOCATION, TailModel::createBodyLayer);

        // Register projectile renderers
        EntityRendererRegistry.register(ModEntityTypes.CYAN_PROJECTILE.get(), ctx -> new ThrownItemRenderer<>(ctx));
        EntityRendererRegistry.register(ModEntityTypes.ORANGE_PROJECTILE.get(), ctx -> new ThrownItemRenderer<>(ctx));

        // Register custom armor renderers for all cat-ears items
        FabricCatEarsRenderer helmRenderer = new FabricCatEarsRenderer(true);
        FabricCatEarsRenderer chestRenderer = new FabricCatEarsRenderer(false);

        ArmorRenderer.register(helmRenderer,
                ModItems.BLACKCATEARS.get(),
                ModItems.BLUECATEARS.get(),
                ModItems.BROWNCATEARS.get(),
                ModItems.CYANCATEARS.get(),
                ModItems.GRAYCATEARS.get(),
                ModItems.GREENCATEARS.get(),
                ModItems.LIGHTBLUECATEARS.get(),
                ModItems.LIGHTGRAYCATEARS.get(),
                ModItems.LIMECATEARS.get(),
                ModItems.MAGENTACATEARS.get(),
                ModItems.ORANGECATEARS.get(),
                ModItems.PINKCATEARS.get(),
                ModItems.PURPLECATEARS.get(),
                ModItems.REDCATEARS.get(),
                ModItems.WHITECATEARS.get(),
                ModItems.YELLOWCATEARS.get());

        ArmorRenderer.register(chestRenderer,
                ModItems.BLACKTAIL.get(),
                ModItems.BLUETAIL.get(),
                ModItems.BROWNTAIL.get(),
                ModItems.CYANTAIL.get(),
                ModItems.GRAYTAIL.get(),
                ModItems.GREENTAIL.get(),
                ModItems.LIGHTBLUETAIL.get(),
                ModItems.LIGHTGRAYTAIL.get(),
                ModItems.LIMETAIL.get(),
                ModItems.MAGENTATAIL.get(),
                ModItems.ORANGETAIL.get(),
                ModItems.PINKTAIL.get(),
                ModItems.PURPLETAIL.get(),
                ModItems.REDTAIL.get(),
                ModItems.WHITETAIL.get(),
                ModItems.YELLOWTAIL.get());

        // Register network receiver for Eepy screen packet (S2C)
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

        // Tick: keep Eepy screen open each client tick while the effect is active
        ClientTickEvents.END_CLIENT_TICK.register(client -> EepyScreen.onClientTick());

        // Optional: register Accessories renderers if the mod is present
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            AccessoriesIntegration.registerRenderers();
        }

        // Register fluid render handlers (tinted water textures)
        ResourceLocation waterStill = new ResourceLocation("minecraft", "block/water_still");
        ResourceLocation waterFlow  = new ResourceLocation("minecraft", "block/water_flow");
        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.ACETONE.get(), ModFluids.ACETONE_FLOWING.get(),
                new SimpleFluidRenderHandler(waterStill, waterFlow, 0xA0D6F5FF)); // pale blue
        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.BLEACH.get(), ModFluids.BLEACH_FLOWING.get(),
                new SimpleFluidRenderHandler(waterStill, waterFlow, 0xA0F5F5FA)); // near white
        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.CHLOROFORM.get(), ModFluids.CHLOROFORM_FLOWING.get(),
                new SimpleFluidRenderHandler(waterStill, waterFlow, 0xA0C8F5D0)); // pale green
        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.VINEGAR.get(), ModFluids.VINEGAR_FLOWING.get(),
                new SimpleFluidRenderHandler(waterStill, waterFlow, 0xA0E8D890)); // yellow-brown
    }
}
