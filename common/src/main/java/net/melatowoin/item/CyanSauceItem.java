package net.melatowoin.item;

import net.melatowoin.entity.CyanProjectileEntity;
import net.melatowoin.registry.ModEntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public class CyanSauceItem extends ThrowableSauceItem {

    public CyanSauceItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrowableProjectile createProjectile(Level level, Player player) {
        return new CyanProjectileEntity(ModEntityTypes.CYAN_PROJECTILE.get(), level, player);
    }
}
