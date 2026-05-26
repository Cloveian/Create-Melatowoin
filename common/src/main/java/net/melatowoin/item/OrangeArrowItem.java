package net.melatowoin.item;

import net.melatowoin.entity.OrangeArrowEntity;
import net.melatowoin.registry.ModEntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OrangeArrowItem extends ArrowItem {

    public OrangeArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        OrangeArrowEntity arrow = new OrangeArrowEntity(ModEntityTypes.ORANGE_ARROW.get(), level, shooter);
        arrow.setSauceStack(stack.copy());
        return arrow;
    }
}
