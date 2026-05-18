package net.melatowoin.forge;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.item.OrangeEquipHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;

public class AccessoriesForgeHelper {

    public static void equipEarsAndTail(LivingEntity entity) {
        if (entity.level().isClientSide()) return;

        var cap = AccessoriesCapability.get(entity);

        // If already wearing ears+tail in any combination of slots → do nothing
        boolean hasEar = OrangeEquipHelper.isEarItem(entity.getItemBySlot(EquipmentSlot.HEAD).getItem())
                || (cap != null && slotHasItemOfType(cap, "hat", true));
        boolean hasTail = OrangeEquipHelper.isTailItem(entity.getItemBySlot(EquipmentSlot.LEGS).getItem())
                || (cap != null && slotHasItemOfType(cap, "belt", false));
        if (hasEar && hasTail) return;

        int colorIndex = OrangeEquipHelper.randomColorIndex(entity);

        // Priority 1: Accessories hat+belt — both free
        if (cap != null && isSlotFree(cap, "hat") && isSlotFree(cap, "belt")) {
            equipAccessory(cap, "hat",  OrangeEquipHelper.earItems().get(colorIndex));
            equipAccessory(cap, "belt", OrangeEquipHelper.tailItems().get(colorIndex));
            OrangeEquipHelper.applyChangeEffect(entity);
            return;
        }

        // Priority 2: Armor HEAD+LEGS — both free
        if (OrangeEquipHelper.armorSlotsAreFree(entity)) {
            OrangeEquipHelper.equipArmorPair(entity, colorIndex);
            OrangeEquipHelper.applyChangeEffect(entity);
            return;
        }

        // Priority 3: Accessories present — swap hat+belt contents to inventory, equip new
        if (cap != null) {
            ItemStack oldHat  = getAndClear(cap, "hat");
            ItemStack oldBelt = getAndClear(cap, "belt");
            OrangeEquipHelper.giveOrDrop(entity, oldHat);
            OrangeEquipHelper.giveOrDrop(entity, oldBelt);
            equipAccessory(cap, "hat",  OrangeEquipHelper.earItems().get(colorIndex));
            equipAccessory(cap, "belt", OrangeEquipHelper.tailItems().get(colorIndex));
            OrangeEquipHelper.applyChangeEffect(entity);
        }
    }

    private static boolean slotHasItemOfType(AccessoriesCapability cap, String slotName, boolean ear) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return false;
        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return false;
        Item item = stacks.getItem(0).getItem();
        return ear ? OrangeEquipHelper.isEarItem(item) : OrangeEquipHelper.isTailItem(item);
    }

    private static boolean isSlotFree(AccessoriesCapability cap, String slotName) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return false;
        var stacks = container.getAccessories();
        return stacks.getContainerSize() > 0 && stacks.getItem(0).isEmpty();
    }

    private static ItemStack getAndClear(AccessoriesCapability cap, String slotName) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return ItemStack.EMPTY;
        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return ItemStack.EMPTY;
        ItemStack existing = stacks.getItem(0).copy();
        stacks.setItem(0, ItemStack.EMPTY);
        return existing;
    }

    private static void equipAccessory(AccessoriesCapability cap, String slotName, Item item) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return;
        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return;
        var stack = new ItemStack(item);
        var enchants = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
        enchants.put(Enchantments.BINDING_CURSE, 1);
        EnchantmentHelper.setEnchantments(enchants, stack);
        stacks.setItem(0, stack);
    }
}
