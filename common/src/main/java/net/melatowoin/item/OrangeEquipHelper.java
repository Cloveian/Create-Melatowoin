package net.melatowoin.item;

import net.melatowoin.registry.ModEffects;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public class OrangeEquipHelper {

    public static boolean isEarItem(net.minecraft.world.item.Item item) {
        return DyeableEquipmentItem.isType(new ItemStack(item), DyeableEquipmentItem.EquipType.CAT_EARS);
    }

    public static boolean isTailItem(net.minecraft.world.item.Item item) {
        return DyeableEquipmentItem.isType(new ItemStack(item), DyeableEquipmentItem.EquipType.TAIL);
    }

    public static boolean wearingArmorEarsAndTail(LivingEntity entity) {
        return isEarItem(entity.getItemBySlot(EquipmentSlot.HEAD).getItem())
                && isTailItem(entity.getItemBySlot(EquipmentSlot.LEGS).getItem());
    }

    public static boolean armorSlotsAreFree(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && entity.getItemBySlot(EquipmentSlot.LEGS).isEmpty();
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

        entity.setItemSlot(EquipmentSlot.HEAD, ears);
        entity.setItemSlot(EquipmentSlot.LEGS, tail);
    }

    public static int randomColorIndex(LivingEntity entity) {
        return entity.getRandom().nextInt(DyeColor.values().length);
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

    public static void defaultEquip(LivingEntity entity) {
        if (entity.level().isClientSide()) return;
        if (wearingArmorEarsAndTail(entity)) return;
        if (!armorSlotsAreFree(entity)) return;
        equipArmorPair(entity, randomColorIndex(entity));
        applyChangeEffect(entity);
    }
}
