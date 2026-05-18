package net.melatowoin.item;

import net.melatowoin.entity.OrangeProjectileEntity;
import net.melatowoin.registry.ModEntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public class OrangeSauceItem extends ThrowableSauceItem {

    public OrangeSauceItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrowableProjectile createProjectile(Level level, Player player) {
        return new OrangeProjectileEntity(ModEntityTypes.ORANGE_PROJECTILE.get(), level, player);
    }
}
