package net.melatowoin.forge;

import dev.architectury.platform.forge.EventBuses;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.entity.OrangeProjectileEntity;
import net.melatowoin.forge.fluid.ForgeAcetoneFluid;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.item.OrangeEquipHelper;
import net.melatowoin.forge.fluid.ForgeBleachFluid;
import net.melatowoin.forge.fluid.ForgeChloroformFluid;
import net.melatowoin.forge.fluid.ForgeVinegarFluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MelatowoinMod.MOD_ID)
public class MelatowoinForge {
    public MelatowoinForge() {
        // Required for Architectury's DeferredRegister to find this mod's event bus
        EventBuses.registerModEventBus(MelatowoinMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Register platform fluids first so bucket items can reference fluid objects
        // (each registers its FluidType DeferredRegister on the mod bus)
        ForgeAcetoneFluid.register();
        ForgeBleachFluid.register();
        ForgeChloroformFluid.register();
        ForgeVinegarFluid.register();

        // Common init: registers items, effects, entities, creative tab, game rules,
        // network packets, and cross-platform events (via Architectury)
        MelatowoinMod.init();

        // Default: equip in armor slots; override with Accessories priority logic if present
        OrangeProjectileEntity.onHitExtra = OrangeEquipHelper::defaultEquip;
        if (ModList.get().isLoaded("accessories")) {
            OrangeProjectileEntity.onHitExtra = (entity, stack) -> AccessoriesForgeHelper.equipEarsAndTail(entity, stack);

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

        // @Mod.EventBusSubscriber classes (ForgeEventHandlers, MelatowoinForgeClient)
        // are discovered automatically by Forge via annotation scanning.
    }
}
