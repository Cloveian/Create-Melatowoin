package net.melatowoin.recipe;

import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Crafting table recipe that dyes DyeableEquipmentItem pieces.
 *
 * Place the item anywhere in the grid and put one dye adjacent to it:
 *   Left or right  → changes the main (fur) color
 *   Above or below → changes the accent (paw pad) color
 *
 * No other items may be in the grid.
 */
public class DyeableEquipmentDyeRecipe extends CustomRecipe {

    public DyeableEquipmentDyeRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int[] pos = findSlots(container);
        return pos != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        int[] pos = findSlots(container);
        if (pos == null) return ItemStack.EMPTY;

        int itemSlot = pos[0];
        int dyeSlot  = pos[1];
        int w = container.getWidth();

        ItemStack result = container.getItem(itemSlot).copy();
        DyeableEquipmentItem dyeable = (DyeableEquipmentItem) result.getItem();
        int color = ((DyeItem) container.getItem(dyeSlot).getItem()).getDyeColor().getFireworkColor();

        int itemRow = itemSlot / w, itemCol = itemSlot % w;
        int dyeRow  = dyeSlot  / w, dyeCol  = dyeSlot  % w;
        boolean leftRight = (itemRow == dyeRow);

        if (leftRight) {
            dyeable.setMainColor(result, color);
        } else {
            dyeable.setAccentColor(result, color);
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 || height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DYEABLE_EQUIPMENT_DYE.get();
    }

    /**
     * Returns [itemSlot, dyeSlot] if the grid contains exactly one DyeableEquipmentItem
     * and one DyeItem that are directly adjacent (left/right or above/below).
     * Returns null if the pattern doesn't match.
     */
    private static int[] findSlots(CraftingContainer container) {
        int w = container.getWidth();
        int itemSlot = -1, dyeSlot = -1;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof DyeableEquipmentItem) {
                if (itemSlot != -1) return null; // two equipment items
                itemSlot = i;
            } else if (stack.getItem() instanceof DyeItem) {
                if (dyeSlot != -1) return null; // two dyes
                dyeSlot = i;
            } else {
                return null; // unexpected item
            }
        }

        if (itemSlot < 0 || dyeSlot < 0) return null;

        int ir = itemSlot / w, ic = itemSlot % w;
        int dr = dyeSlot  / w, dc = dyeSlot  % w;
        boolean adjacent = (ir == dr && Math.abs(ic - dc) == 1)   // left/right
                        || (ic == dc && Math.abs(ir - dr) == 1);   // above/below
        return adjacent ? new int[]{itemSlot, dyeSlot} : null;
    }
}
