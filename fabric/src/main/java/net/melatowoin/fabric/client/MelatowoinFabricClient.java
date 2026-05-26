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
import net.melatowoin.client.model.PawsModel;
import net.melatowoin.client.model.TailModel;
import net.melatowoin.client.model.ToeBeansModel;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.client.screen.EepyScreen;
import net.melatowoin.fabric.client.renderer.FabricCatEarsRenderer;
import net.melatowoin.item.DyeableEquipmentItem;
import io.wispforest.accessories.api.AccessoriesCapability;
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
        EntityModelLayerRegistry.registerModelLayer(CatEarModel.LAYER_LOCATION,   CatEarModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(TailModel.LAYER_LOCATION,     TailModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PawsModel.LAYER_LOCATION,     PawsModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ToeBeansModel.LAYER_LOCATION, ToeBeansModel::createBodyLayer);

        // Hook for first-person paw rendering when paws are in the Accessories hand slot
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

        // Hook so the boot-suppression mixin can see Toe Beans in the Accessories shoes slot
        AccessoriesSlotHelper.findToeBeansInAccessories = player -> {
            var cap = AccessoriesCapability.get(player);
            if (cap == null) return net.minecraft.world.item.ItemStack.EMPTY;
            var container = cap.getContainers().get("shoes");
            if (container == null) return net.minecraft.world.item.ItemStack.EMPTY;
            var stacks = container.getAccessories();
            for (int i = 0; i < stacks.getContainerSize(); i++) {
                var s = stacks.getItem(i);
                if (s.getItem() instanceof DyeableEquipmentItem d
                        && d.getEquipType() == DyeableEquipmentItem.EquipType.TOE_BEANS) return s;
            }
            return net.minecraft.world.item.ItemStack.EMPTY;
        };

        // Register projectile renderers
        EntityRendererRegistry.register(ModEntityTypes.CYAN_PROJECTILE.get(), ctx -> new ThrownItemRenderer<>(ctx));
        EntityRendererRegistry.register(ModEntityTypes.ORANGE_PROJECTILE.get(), ctx -> new ThrownItemRenderer<>(ctx));
        EntityRendererRegistry.register(ModEntityTypes.ORANGE_ARROW.get(), ctx ->
                new net.minecraft.client.renderer.entity.ArrowRenderer<net.melatowoin.entity.OrangeArrowEntity>(ctx) {
                    @Override
                    public net.minecraft.resources.ResourceLocation getTextureLocation(net.melatowoin.entity.OrangeArrowEntity e) {
                        return new net.minecraft.resources.ResourceLocation("textures/entity/projectiles/arrow.png");
                    }
                });

        // Register one armor renderer for all four cat equipment items
        FabricCatEarsRenderer equipRenderer = new FabricCatEarsRenderer();
        ArmorRenderer.register(equipRenderer,
                ModItems.CAT_EARS.get(), ModItems.PAWS.get(),
                ModItems.TAIL.get(),     ModItems.TOE_BEANS.get());

        // Item tint: layer 0 → main color, layer 1 → accent color
        net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> {
                    if (stack.getItem() instanceof net.melatowoin.item.DyeableEquipmentItem d) {
                        return tintIndex == 0 ? d.getMainColor(stack) : d.getAccentColor(stack);
                    }
                    return -1;
                },
                ModItems.CAT_EARS.get(), ModItems.PAWS.get(),
                ModItems.TAIL.get(),     ModItems.TOE_BEANS.get());

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
