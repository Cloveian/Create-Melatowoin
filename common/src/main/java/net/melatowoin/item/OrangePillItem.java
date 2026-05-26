package net.melatowoin.item;

import net.melatowoin.entity.OrangeProjectileEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OrangePillItem extends Item {

    public OrangePillItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack copy = stack.copy();
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) {
            OrangeProjectileEntity.onHitExtra.accept(entity, copy);
        }
        return result;
    }
}
