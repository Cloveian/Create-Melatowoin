package net.melatowoin.event;

import net.melatowoin.registry.ModGameRules;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Platform-agnostic logic for handling a player right-clicking a Cat while holding a Brush.
 * Platform-specific event listeners (Fabric / Forge) delegate here.
 */
public class CatBrushHandler {

    /**
     * Handle a UseEntity interaction.
     *
     * @return true if the event was consumed and should cancel further processing.
     */
    public static boolean handle(Player player, Entity target, Level level) {
        if (level.isClientSide()) return false;

        // Must be a Cat
        if (!(target instanceof Cat cat)) return false;

        // Player must be holding a Brush in either hand
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

        // Check gamerule
        if (!level.getGameRules().getRule(ModGameRules.DO_CAT).get()) {
            return false;
        }

        // Check brush cooldown for this player
        if (player.getCooldowns().isOnCooldown(brushStack.getItem())) {
            return false;
        }

        // Apply 100-tick cooldown to the Brush item
        player.getCooldowns().addCooldown(brushStack.getItem(), 100);

        // Damage the brush by 8
        brushStack.hurtAndBreak(8, player, p -> p.broadcastBreakEvent(
                player.getMainHandItem() == brushStack
                        ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                        : net.minecraft.world.entity.EquipmentSlot.OFFHAND));

        // Drop a Fur item at the cat's position with 20-tick pickup delay
        ItemEntity furDrop = new ItemEntity(
                level,
                cat.getX(), cat.getY(), cat.getZ(),
                new ItemStack(ModItems.FUR.get()));
        furDrop.setPickUpDelay(20);
        level.addFreshEntity(furDrop);

        return true;
    }
}
