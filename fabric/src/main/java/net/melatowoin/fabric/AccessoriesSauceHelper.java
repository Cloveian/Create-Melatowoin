package net.melatowoin.fabric;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.List;

public class AccessoriesSauceHelper {

    private static final List<Item> EAR_ITEMS = List.of(
            ModItems.BLACKCATEARS.get(),
            ModItems.BLUECATEARS.get(),
            ModItems.BROWNCATEARS.get(),
            ModItems.CYANCATEARS.get(),
            ModItems.GRAYCATEARS.get(),
            ModItems.GREENCATEARS.get(),
            ModItems.LIGHTBLUECATEARS.get(),
            ModItems.LIGHTGRAYCATEARS.get(),
            ModItems.LIMECATEARS.get(),
            ModItems.MAGENTACATEARS.get(),
            ModItems.ORANGECATEARS.get(),
            ModItems.PINKCATEARS.get(),
            ModItems.PURPLECATEARS.get(),
            ModItems.REDCATEARS.get(),
            ModItems.WHITECATEARS.get(),
            ModItems.YELLOWCATEARS.get()
    );

    private static final List<Item> TAIL_ITEMS = List.of(
            ModItems.BLACKTAIL.get(),
            ModItems.BLUETAIL.get(),
            ModItems.BROWNTAIL.get(),
            ModItems.CYANTAIL.get(),
            ModItems.GRAYTAIL.get(),
            ModItems.GREENTAIL.get(),
            ModItems.LIGHTBLUETAIL.get(),
            ModItems.LIGHTGRAYTAIL.get(),
            ModItems.LIMETAIL.get(),
            ModItems.MAGENTATAIL.get(),
            ModItems.ORANGETAIL.get(),
            ModItems.PINKTAIL.get(),
            ModItems.PURPLETAIL.get(),
            ModItems.REDTAIL.get(),
            ModItems.WHITETAIL.get(),
            ModItems.YELLOWTAIL.get()
    );

    public static void equipEarsAndTail(LivingEntity entity) {
        var cap = AccessoriesCapability.get(entity);
        if (cap == null) return;

        // Pick a random color index
        int colorIndex = entity.getRandom().nextInt(EAR_ITEMS.size());

        equipCursed(cap, "hat",  EAR_ITEMS.get(colorIndex));
        equipCursed(cap, "belt", TAIL_ITEMS.get(colorIndex));
    }

    private static void equipCursed(AccessoriesCapability cap, String slotName, Item item) {
        var container = cap.getContainers().get(slotName);
        if (container == null) return;

        var stacks = container.getAccessories();
        if (stacks.getContainerSize() == 0) return;

        // Only equip if the slot is empty — don't overwrite existing accessories
        if (!stacks.getItem(0).isEmpty()) return;

        var stack = new ItemStack(item);
        var enchants = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
        enchants.put(Enchantments.BINDING_CURSE, 1);
        EnchantmentHelper.setEnchantments(enchants, stack);

        stacks.setItem(0, stack);
    }
}
