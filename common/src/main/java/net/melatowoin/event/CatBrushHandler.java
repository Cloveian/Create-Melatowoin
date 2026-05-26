package net.melatowoin.event;

import net.melatowoin.registry.ModGameRules;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CatBrushHandler {

    public static boolean handle(Player player, Entity target, Level level) {
        if (level.isClientSide()) return false;

        if (!(target instanceof Cat cat)) return false;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        ItemStack brushStack;
        if (mainHand.getItem() instanceof BrushItem) {
            brushStack = mainHand;
        } else if (offHand.getItem() instanceof BrushItem) {
            brushStack = offHand;
        } else {
            return false;
        }

        if (!level.getGameRules().getRule(ModGameRules.DO_CAT).get()) {
            return false;
        }

        brushStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(
                player.getMainHandItem() == brushStack
                        ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                        : net.minecraft.world.entity.EquipmentSlot.OFFHAND));

        player.addItem(new ItemStack(ModItems.FUR.get()));

        return true;
    }
}
