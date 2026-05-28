package net.melatowoin.fabric;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.entity.OrangeProjectileEntity;
import net.melatowoin.fabric.event.FabricEventHandlers;
import net.melatowoin.fabric.fluid.FabricFluids;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.item.OrangeEquipHelper;

public class MelatowoinFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register platform fluids before common init so bucket items can reference them
        FabricFluids.register();

        MelatowoinMod.init();

        // Register server-side event handlers
        FabricEventHandlers.register();

        // Default: equip in armor slots; override with Accessories priority logic if present
        OrangeProjectileEntity.onHitExtra = OrangeEquipHelper::defaultEquip;
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            OrangeProjectileEntity.onHitExtra = (entity, stack) -> AccessoriesSauceHelper.equipEarsAndTail(entity, stack);

            // Server-relevant hook: lets common mixins (e.g. powder-snow walking) see
            // Toe Beans in the Accessories shoes slot. Registered here instead of in
            // the client init so it works on dedicated servers too.
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
        }
    }
}
