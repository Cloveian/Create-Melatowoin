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

            // Server-relevant hooks: let common mixins (powder-snow walking, freezing,
            // full-set sound suppression) see each piece in its Accessories slot.
            // Registered here instead of in the client init so dedicated servers see them too.
            AccessoriesSlotHelper.findCatEarsInAccessories  = accessoryFinder(DyeableEquipmentItem.EquipType.CAT_EARS);
            AccessoriesSlotHelper.findTailInAccessories     = accessoryFinder(DyeableEquipmentItem.EquipType.TAIL);
            AccessoriesSlotHelper.findPawsInAccessories     = accessoryFinder(DyeableEquipmentItem.EquipType.PAWS);
            AccessoriesSlotHelper.findToeBeansInAccessories = accessoryFinder(DyeableEquipmentItem.EquipType.TOE_BEANS);
        }
    }

    /**
     * Scans every Accessories container for a stack of the given cat-piece type.
     * Different modpacks (Aether, etc.) put items in different slot names, so
     * looking up by a fixed slot name misses cases. Scanning the whole container
     * map is bounded and cheap.
     */
    private static java.util.function.Function<net.minecraft.world.entity.player.Player, net.minecraft.world.item.ItemStack>
            accessoryFinder(DyeableEquipmentItem.EquipType type) {
        return player -> {
            var cap = AccessoriesCapability.get(player);
            if (cap == null) return net.minecraft.world.item.ItemStack.EMPTY;
            for (var container : cap.getContainers().values()) {
                var stacks = container.getAccessories();
                for (int i = 0; i < stacks.getContainerSize(); i++) {
                    var s = stacks.getItem(i);
                    if (s.getItem() instanceof DyeableEquipmentItem d && d.getEquipType() == type) return s;
                }
            }
            return net.minecraft.world.item.ItemStack.EMPTY;
        };
    }
}
