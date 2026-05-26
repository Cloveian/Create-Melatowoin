package net.melatowoin.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

/**
 * Platform hook so the common mixin can check Accessories slots without
 * importing Accessories directly. Fabric/Forge client init registers the impl.
 */
public class AccessoriesSlotHelper {
    public static Function<Player, ItemStack> findPawsInAccessories = p -> ItemStack.EMPTY;
    public static Function<Player, ItemStack> findToeBeansInAccessories = p -> ItemStack.EMPTY;
}
