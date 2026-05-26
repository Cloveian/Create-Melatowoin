package net.melatowoin.fabric;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.item.OrangeColorData;
import net.melatowoin.item.OrangeEquipHelper;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public class AccessoriesSauceHelper {

    public static void equipEarsAndTail(LivingEntity entity, net.minecraft.world.item.ItemStack source) {
        OrangeColorData colors = OrangeColorData.fromStack(source);
        if (entity.level().isClientSide()) return;

        var cap = AccessoriesCapability.get(entity);

        boolean hasEar = OrangeEquipHelper.isEarItem(entity.getItemBySlot(EquipmentSlot.HEAD).getItem())
                || (cap != null && slotHasItemOfType(cap, "hat", DyeableEquipmentItem.EquipType.CAT_EARS));
        boolean hasTail = OrangeEquipHelper.isTailItem(entity.getItemBySlot(EquipmentSlot.LEGS).getItem())
                || (cap != null && slotHasItemOfType(cap, "belt", DyeableEquipmentItem.EquipType.TAIL));
        boolean hasPaws = OrangeEquipHelper.isPawsItem(entity.getItemBySlot(EquipmentSlot.CHEST).getItem())
                || (cap != null && slotHasItemOfType(cap, "hand", DyeableEquipmentItem.EquipType.PAWS));
        boolean hasToeBeans = OrangeEquipHelper.isToeBeansItem(entity.getItemBySlot(EquipmentSlot.FEET).getItem())
                || (cap != null && slotHasItemOfType(cap, "shoes", DyeableEquipmentItem.EquipType.TOE_BEANS));
        if (hasEar && hasTail && hasPaws && hasToeBeans) return;

        if (colors == null) colors = OrangeEquipHelper.randomColors(entity);

        if (cap != null && isSlotFree(cap, "hat") && isSlotFree(cap, "belt")
                && isSlotFree(cap, "hand") && isSlotFree(cap, "shoes")) {
            equipAccessory(cap, "hat",    colors, DyeableEquipmentItem.EquipType.CAT_EARS);
            equipAccessory(cap, "belt",   colors, DyeableEquipmentItem.EquipType.TAIL);
            equipAccessory(cap, "hand", colors, DyeableEquipmentItem.EquipType.PAWS);
            equipAccessory(cap, "shoes",   colors, DyeableEquipmentItem.EquipType.TOE_BEANS);
            OrangeEquipHelper.applyChangeEffect(entity);
            return;
        }

        if (OrangeEquipHelper.armorSlotsAreFree(entity)) {
            OrangeEquipHelper.equipArmorPairColored(entity, colors);
            OrangeEquipHelper.applyChangeEffect(entity);
            return;
        }

        if (cap != null) {
            ItemStack oldHat    = getAndClear(cap, "hat");
            ItemStack oldBelt   = getAndClear(cap, "belt");
            ItemStack oldGloves = getAndClear(cap, "hand");
            ItemStack oldFeet   = getAndClear(cap, "shoes");
            OrangeEquipHelper.giveOrDrop(entity, oldHat);
            OrangeEquipHelper.giveOrDrop(entity, oldBelt);
            OrangeEquipHelper.giveOrDrop(entity, oldGloves);
            OrangeEquipHelper.giveOrDrop(entity, oldFeet);
            equipAccessory(cap, "hat",    colors, DyeableEquipmentItem.EquipType.CAT_EARS);
            equipAccessory(cap, "belt",   colors, DyeableEquipmentItem.EquipType.TAIL);
            equipAccessory(cap, "hand", colors, DyeableEquipmentItem.EquipType.PAWS);
            equipAccessory(cap, "shoes",   colors, DyeableEquipmentItem.EquipType.TOE_BEANS);
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
                                        OrangeColorData colors,
                                        DyeableEquipmentItem.EquipType type) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return;
        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return;

        net.minecraft.world.item.Item baseItem = switch (type) {
            case CAT_EARS  -> ModItems.CAT_EARS.get();
            case TAIL      -> ModItems.TAIL.get();
            case PAWS      -> ModItems.PAWS.get();
            case TOE_BEANS -> ModItems.TOE_BEANS.get();
        };

        ItemStack stack = new ItemStack(baseItem);
        int mainColor = switch (type) {
            case CAT_EARS  -> colors.earMain();
            case TAIL      -> colors.tailMain();
            case PAWS      -> colors.pawsMain();
            case TOE_BEANS -> colors.toeMain();
        };
        int accentColor = switch (type) {
            case CAT_EARS  -> colors.earAccent();
            case TAIL      -> colors.tailAccent();
            case PAWS      -> colors.pawsAccent();
            case TOE_BEANS -> colors.toeAccent();
        };
        ((DyeableEquipmentItem) stack.getItem()).setMainColor(stack, mainColor);
        ((DyeableEquipmentItem) stack.getItem()).setAccentColor(stack, accentColor);

        var enchants = new java.util.HashMap<>(
                net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantments(stack));
        enchants.put(Enchantments.BINDING_CURSE, 1);
        net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(enchants, stack);
        stacks.setItem(0, stack);
    }
}
