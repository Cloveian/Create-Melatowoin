package net.melatowoin.fabric;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.item.OrangeEquipHelper;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public class AccessoriesSauceHelper {

    public static void equipEarsAndTail(LivingEntity entity) {
        if (entity.level().isClientSide()) return;

        var cap = AccessoriesCapability.get(entity);

        boolean hasEar = OrangeEquipHelper.isEarItem(entity.getItemBySlot(EquipmentSlot.HEAD).getItem())
                || (cap != null && slotHasItemOfType(cap, "hat", DyeableEquipmentItem.EquipType.CAT_EARS));
        boolean hasTail = OrangeEquipHelper.isTailItem(entity.getItemBySlot(EquipmentSlot.LEGS).getItem())
                || (cap != null && slotHasItemOfType(cap, "belt", DyeableEquipmentItem.EquipType.TAIL));
        if (hasEar && hasTail) return;

        int colorIndex = OrangeEquipHelper.randomColorIndex(entity);

        if (cap != null && isSlotFree(cap, "hat") && isSlotFree(cap, "belt")) {
            equipAccessory(cap, "hat",  colorIndex, DyeableEquipmentItem.EquipType.CAT_EARS);
            equipAccessory(cap, "belt", colorIndex, DyeableEquipmentItem.EquipType.TAIL);
            OrangeEquipHelper.applyChangeEffect(entity);
            return;
        }

        if (OrangeEquipHelper.armorSlotsAreFree(entity)) {
            OrangeEquipHelper.equipArmorPair(entity, colorIndex);
            OrangeEquipHelper.applyChangeEffect(entity);
            return;
        }

        if (cap != null) {
            ItemStack oldHat  = getAndClear(cap, "hat");
            ItemStack oldBelt = getAndClear(cap, "belt");
            OrangeEquipHelper.giveOrDrop(entity, oldHat);
            OrangeEquipHelper.giveOrDrop(entity, oldBelt);
            equipAccessory(cap, "hat",  colorIndex, DyeableEquipmentItem.EquipType.CAT_EARS);
            equipAccessory(cap, "belt", colorIndex, DyeableEquipmentItem.EquipType.TAIL);
            OrangeEquipHelper.applyChangeEffect(entity);
        }
    }

    private static boolean slotHasItemOfType(AccessoriesCapability cap, String slotName,
                                              DyeableEquipmentItem.EquipType type) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return false;
        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return false;
        return DyeableEquipmentItem.isType(stacks.getItem(0), type);
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

    private static void equipAccessory(AccessoriesCapability cap, String slotName,
                                        int colorIndex, DyeableEquipmentItem.EquipType type) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return;
        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return;

        net.minecraft.world.item.Item baseItem = switch (type) {
            case CAT_EARS -> ModItems.CAT_EARS.get();
            case TAIL     -> ModItems.TAIL.get();
            default       -> ModItems.CAT_EARS.get();
        };

        ItemStack stack = new ItemStack(baseItem);
        DyeColor[] colors = DyeColor.values();
        DyeColor main   = colors[colorIndex % colors.length];
        DyeColor accent = colors[(colorIndex + 8) % colors.length];
        ((DyeableEquipmentItem) stack.getItem()).setMainColor(stack, main.getFireworkColor());
        ((DyeableEquipmentItem) stack.getItem()).setAccentColor(stack, accent.getFireworkColor());

        var enchants = new java.util.HashMap<>(
                net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantments(stack));
        enchants.put(Enchantments.BINDING_CURSE, 1);
        net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(enchants, stack);
        stacks.setItem(0, stack);
    }
}
