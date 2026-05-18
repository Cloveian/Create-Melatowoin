package net.melatowoin.effect;

import net.melatowoin.registry.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public class ChangeEffect extends MobEffect {

    public ChangeEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFFB000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Not used — behavior is driven by applyEffect called on start
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    /**
     * Called when the effect is first applied (by our platform event hook).
     * Force-equips orangecatears helmet and chestplate with Curse of Binding.
     */
    public static void onStarted(LivingEntity entity) {
        if (entity.level().isClientSide()) return;

        // Build helmets with Curse of Binding
        ItemStack helmet = new ItemStack(ModItems.ORANGECATEARS.get());
        helmet.enchant(Enchantments.BINDING_CURSE, 1);

        ItemStack chestplate = new ItemStack(ModItems.ORANGETAIL.get());
        chestplate.enchant(Enchantments.BINDING_CURSE, 1);

        entity.setItemSlot(EquipmentSlot.HEAD, helmet);
        entity.setItemSlot(EquipmentSlot.CHEST, chestplate);
    }
}
