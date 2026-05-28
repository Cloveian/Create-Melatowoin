package net.melatowoin.forge.client;

import dev.architectury.networking.NetworkManager;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.client.model.CatEarModel;
import net.melatowoin.client.model.PawsModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.model.ToeBeansModel;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.client.screen.EepyScreen;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.forge.client.renderer.CatEarsLayer;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.network.EepyScreenPacket;
import net.melatowoin.registry.ModEntityTypes;
import net.melatowoin.registry.ModItems;
import net.minecraftforge.fml.ModList;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
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
            AccessoriesSlotHelper.findPawsInAccessories = player -> {
                var cap = AccessoriesCapability.get(player);
                if (cap == null) return net.minecraft.world.item.ItemStack.EMPTY;
                var container = cap.getContainers().get("hand");
                if (container == null) return net.minecraft.world.item.ItemStack.EMPTY;
                var stacks = container.getAccessories();
                for (int i = 0; i < stacks.getContainerSize(); i++) {
                    var s = stacks.getItem(i);
                    if (s.getItem() instanceof DyeableEquipmentItem d
                            && d.getEquipType() == DyeableEquipmentItem.EquipType.PAWS) return s;
                }
                return net.minecraft.world.item.ItemStack.EMPTY;
            };

            // (findToeBeansInAccessories is registered in MelatowoinForge's constructor
            // so it works on dedicated servers too — see powder-snow walking)
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
        event.registerEntityRenderer(ModEntityTypes.ORANGE_ARROW.get(), ctx ->
                new net.minecraft.client.renderer.entity.ArrowRenderer<net.melatowoin.entity.OrangeArrowEntity>(ctx) {
                    @Override
                    public net.minecraft.resources.ResourceLocation getTextureLocation(net.melatowoin.entity.OrangeArrowEntity e) {
                        return new net.minecraft.resources.ResourceLocation("textures/entity/projectiles/arrow.png");
                    }
                });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CatEarModel.LAYER_LOCATION,   CatEarModel::createBodyLayer);
        event.registerLayerDefinition(TailModel.LAYER_LOCATION,     TailModel::createBodyLayer);
        event.registerLayerDefinition(PawsModel.LAYER_LOCATION,     PawsModel::createBodyLayer);
        event.registerLayerDefinition(ToeBeansModel.LAYER_LOCATION, ToeBeansModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> {
                    if (stack.getItem() instanceof DyeableEquipmentItem d) {
                        return tintIndex == 0 ? d.getMainColor(stack) : d.getAccentColor(stack);
                    }
                    return -1;
                },
                ModItems.CAT_EARS.get(), ModItems.PAWS.get(),
                ModItems.TAIL.get(),     ModItems.TOE_BEANS.get());
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
