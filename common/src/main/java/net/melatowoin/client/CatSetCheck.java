package net.melatowoin.client;

import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Cross-loader helper that asks "does this player wear the full cat outfit
 * (Cat Ears + Tail + Paws + Toe Beans)?", checking both the four armor slots
 * and the four Accessories slots via the platform-registered hooks on
 * {@link AccessoriesSlotHelper}.
 */
public class CatSetCheck {

    public static boolean hasFullSet(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            // Mobs only ever wear armor; no accessory containers to check.
            return DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.HEAD),  DyeableEquipmentItem.EquipType.CAT_EARS)
                && DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.LEGS),  DyeableEquipmentItem.EquipType.TAIL)
                && DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.CHEST), DyeableEquipmentItem.EquipType.PAWS)
                && DyeableEquipmentItem.isType(entity.getItemBySlot(EquipmentSlot.FEET),  DyeableEquipmentItem.EquipType.TOE_BEANS);
        }
        return hasCatEars(player) && hasTail(player) && hasPaws(player) && hasToeBeans(player);
    }

    public static boolean hasCatEars(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.HEAD),
                DyeableEquipmentItem.EquipType.CAT_EARS)) return true;
        ItemStack acc = AccessoriesSlotHelper.findCatEarsInAccessories.apply(player);
        return DyeableEquipmentItem.isType(acc, DyeableEquipmentItem.EquipType.CAT_EARS);
    }

    public static boolean hasTail(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.LEGS),
                DyeableEquipmentItem.EquipType.TAIL)) return true;
        ItemStack acc = AccessoriesSlotHelper.findTailInAccessories.apply(player);
        return DyeableEquipmentItem.isType(acc, DyeableEquipmentItem.EquipType.TAIL);
    }

    public static boolean hasPaws(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.CHEST),
                DyeableEquipmentItem.EquipType.PAWS)) return true;
        ItemStack acc = AccessoriesSlotHelper.findPawsInAccessories.apply(player);
        return DyeableEquipmentItem.isType(acc, DyeableEquipmentItem.EquipType.PAWS);
    }

    public static boolean hasToeBeans(Player player) {
        if (DyeableEquipmentItem.isType(player.getItemBySlot(EquipmentSlot.FEET),
                DyeableEquipmentItem.EquipType.TOE_BEANS)) return true;
        ItemStack acc = AccessoriesSlotHelper.findToeBeansInAccessories.apply(player);
        return DyeableEquipmentItem.isType(acc, DyeableEquipmentItem.EquipType.TOE_BEANS);
    }
}
