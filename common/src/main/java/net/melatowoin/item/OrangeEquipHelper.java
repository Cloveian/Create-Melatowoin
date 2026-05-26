package net.melatowoin.item;

import net.melatowoin.registry.ModEffects;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;

public class OrangeEquipHelper {

    public static boolean isEarItem(net.minecraft.world.item.Item item) {
        return DyeableEquipmentItem.isType(new ItemStack(item), DyeableEquipmentItem.EquipType.CAT_EARS);
    }

    public static boolean isTailItem(net.minecraft.world.item.Item item) {
        return DyeableEquipmentItem.isType(new ItemStack(item), DyeableEquipmentItem.EquipType.TAIL);
    }

    public static boolean isPawsItem(net.minecraft.world.item.Item item) {
        return DyeableEquipmentItem.isType(new ItemStack(item), DyeableEquipmentItem.EquipType.PAWS);
    }

    public static boolean isToeBeansItem(net.minecraft.world.item.Item item) {
        return DyeableEquipmentItem.isType(new ItemStack(item), DyeableEquipmentItem.EquipType.TOE_BEANS);
    }

    public static boolean wearingArmorEarsAndTail(LivingEntity entity) {
        return isEarItem(entity.getItemBySlot(EquipmentSlot.HEAD).getItem())
                && isTailItem(entity.getItemBySlot(EquipmentSlot.LEGS).getItem())
                && isPawsItem(entity.getItemBySlot(EquipmentSlot.CHEST).getItem())
                && isToeBeansItem(entity.getItemBySlot(EquipmentSlot.FEET).getItem());
    }

    public static boolean armorSlotsAreFree(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && entity.getItemBySlot(EquipmentSlot.LEGS).isEmpty()
                && entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                && entity.getItemBySlot(EquipmentSlot.FEET).isEmpty();
    }

    public static void equipArmorPair(LivingEntity entity, int colorIndex) {
        DyeColor[] colors = DyeColor.values();
        DyeColor mainColor   = colors[colorIndex % colors.length];
        DyeColor accentColor = colors[(colorIndex + 8) % colors.length]; // offset for contrast

        ItemStack ears = new ItemStack(ModItems.CAT_EARS.get());
        ((DyeableEquipmentItem) ears.getItem()).setMainColor(ears, mainColor.getFireworkColor());
        ((DyeableEquipmentItem) ears.getItem()).setAccentColor(ears, accentColor.getFireworkColor());
        ears.enchant(Enchantments.BINDING_CURSE, 1);

        ItemStack tail = new ItemStack(ModItems.TAIL.get());
        ((DyeableEquipmentItem) tail.getItem()).setMainColor(tail, mainColor.getFireworkColor());
        ((DyeableEquipmentItem) tail.getItem()).setAccentColor(tail, accentColor.getFireworkColor());
        tail.enchant(Enchantments.BINDING_CURSE, 1);

        ItemStack paws = new ItemStack(ModItems.PAWS.get());
        ((DyeableEquipmentItem) paws.getItem()).setMainColor(paws, mainColor.getFireworkColor());
        ((DyeableEquipmentItem) paws.getItem()).setAccentColor(paws, accentColor.getFireworkColor());
        paws.enchant(Enchantments.BINDING_CURSE, 1);

        ItemStack toeBeans = new ItemStack(ModItems.TOE_BEANS.get());
        ((DyeableEquipmentItem) toeBeans.getItem()).setMainColor(toeBeans, mainColor.getFireworkColor());
        ((DyeableEquipmentItem) toeBeans.getItem()).setAccentColor(toeBeans, accentColor.getFireworkColor());
        toeBeans.enchant(Enchantments.BINDING_CURSE, 1);

        entity.setItemSlot(EquipmentSlot.HEAD, ears);
        entity.setItemSlot(EquipmentSlot.LEGS, tail);
        entity.setItemSlot(EquipmentSlot.CHEST, paws);
        entity.setItemSlot(EquipmentSlot.FEET, toeBeans);
    }

    public static OrangeColorData randomColors(LivingEntity entity) {
        DyeColor[] dyes = DyeColor.values();
        DyeColor main = dyes[entity.getRandom().nextInt(dyes.length)];
        int mainRgb   = main.getFireworkColor();
        int accentRgb = isLightColor(main)
                ? DyeColor.BLACK.getFireworkColor()
                : DyeColor.WHITE.getFireworkColor();
        return new OrangeColorData(mainRgb, accentRgb, mainRgb, accentRgb,
                                   mainRgb, accentRgb, mainRgb, accentRgb);
    }

    private static boolean isLightColor(DyeColor color) {
        return color == DyeColor.WHITE || color == DyeColor.GRAY || color == DyeColor.LIGHT_GRAY;
    }

    public static void giveOrDrop(LivingEntity entity, ItemStack stack) {
        if (stack.isEmpty()) return;
        if (entity instanceof Player player && player.getInventory().add(stack)) return;
        entity.level().addFreshEntity(
                new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack));
    }

    public static void applyChangeEffect(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(ModEffects.CHANGE.get(), 4000, 0));
    }

    public static void defaultEquip(LivingEntity entity, ItemStack source) {
        if (entity.level().isClientSide()) return;
        if (wearingArmorEarsAndTail(entity)) return;
        if (!armorSlotsAreFree(entity)) return;
        OrangeColorData colors = OrangeColorData.fromStack(source);
        if (colors == null) colors = randomColors(entity);
        equipArmorPairColored(entity, colors);
        applyChangeEffect(entity);
    }

    public static void equipArmorPairColored(LivingEntity entity, OrangeColorData colors) {
        entity.setItemSlot(EquipmentSlot.HEAD,  makeColoredItem(ModItems.CAT_EARS.get(),  colors.earMain(),  colors.earAccent()));
        entity.setItemSlot(EquipmentSlot.LEGS,  makeColoredItem(ModItems.TAIL.get(),       colors.tailMain(), colors.tailAccent()));
        entity.setItemSlot(EquipmentSlot.CHEST, makeColoredItem(ModItems.PAWS.get(),       colors.pawsMain(), colors.pawsAccent()));
        entity.setItemSlot(EquipmentSlot.FEET,  makeColoredItem(ModItems.TOE_BEANS.get(), colors.toeMain(),  colors.toeAccent()));
    }

    private static ItemStack makeColoredItem(Item baseItem, int main, int accent) {
        ItemStack stack = new ItemStack(baseItem);
        ((DyeableEquipmentItem) stack.getItem()).setMainColor(stack, main);
        ((DyeableEquipmentItem) stack.getItem()).setAccentColor(stack, accent);
        stack.enchant(Enchantments.BINDING_CURSE, 1);
        return stack;
    }
}
