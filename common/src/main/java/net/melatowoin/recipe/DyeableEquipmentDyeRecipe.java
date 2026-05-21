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

import java.util.ArrayList;
import java.util.List;

/**
 * Crafting table recipe that dyes DyeableEquipmentItem pieces.
 *
 * Place the equipment item anywhere in the grid, then place dyes adjacent to it:
 *   Dyes left or right  → mixed into the main (fur) color
 *   Dyes above or below → mixed into the accent (paw pad / inner ear) color
 *
 * Multiple dyes on the same side are blended using the Java Edition leather-armor
 * formula. No other item types may appear in the grid.
 */
public class DyeableEquipmentDyeRecipe extends CustomRecipe {

    public DyeableEquipmentDyeRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return findItemSlot(container) >= 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        int itemSlot = findItemSlot(container);
        if (itemSlot < 0) return ItemStack.EMPTY;

        int w = container.getWidth();
        int itemRow = itemSlot / w, itemCol = itemSlot % w;

        List<int[]> mainDyes   = new ArrayList<>();
        List<int[]> accentDyes = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty() || i == itemSlot) continue;
            if (!(stack.getItem() instanceof DyeItem dye)) continue;

            float[] rgb = dye.getDyeColor().getTextureDiffuseColors();
            int[] c = {
                (int)(rgb[0] * 255f),
                (int)(rgb[1] * 255f),
                (int)(rgb[2] * 255f)
            };

            int dr = i / w, dc = i % w;
            if (dr == itemRow) {
                mainDyes.add(c);   // same row = left/right
            } else {
                accentDyes.add(c); // different row = above/below
            }
        }

        if (mainDyes.isEmpty() && accentDyes.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = container.getItem(itemSlot).copy();
        DyeableEquipmentItem dyeable = (DyeableEquipmentItem) result.getItem();

        if (!mainDyes.isEmpty()) {
            int mixed = mix(dyeable.isMainDyed(result), dyeable.getMainColor(result), mainDyes);
            dyeable.setMainColor(result, mixed);
            dyeable.setMainDyed(result, true);
        }
        if (!accentDyes.isEmpty()) {
            int mixed = mix(dyeable.isAccentDyed(result), dyeable.getAccentColor(result), accentDyes);
            dyeable.setAccentColor(result, mixed);
            dyeable.setAccentDyed(result, true);
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
     * Returns the slot index of the DyeableEquipmentItem in the grid, or -1 if invalid.
     * Validates: exactly one equipment item, one or more dyes all adjacent to it, nothing else.
     */
    private static int findItemSlot(CraftingContainer container) {
        int w = container.getWidth();
        int itemSlot = -1;
        boolean hasDye = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof DyeableEquipmentItem) {
                if (itemSlot != -1) return -1; // two equipment items
                itemSlot = i;
            } else if (!(stack.getItem() instanceof DyeItem)) {
                return -1; // unexpected item type
            }
        }
        if (itemSlot < 0) return -1;

        int itemRow = itemSlot / w, itemCol = itemSlot % w;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty() || i == itemSlot) continue;
            if (!(stack.getItem() instanceof DyeItem)) return -1;

            int dr = i / w, dc = i % w;
            boolean leftRight  = (dr == itemRow && Math.abs(dc - itemCol) == 1);
            boolean aboveBelow = (dc == itemCol && Math.abs(dr - itemRow) == 1);
            if (!leftRight && !aboveBelow) return -1; // dye not adjacent
            hasDye = true;
        }
        return hasDye ? itemSlot : -1;
    }

    /**
     * Java Edition leather-armor color mixing formula.
     *
     * Matches DyeableLeatherItem.dyeArmor() exactly:
     *   factor = mean(pmax per color) / max(meanR, meanG, meanB)
     *   outChannel = floor(meanChannel * factor)
     */
    static int mix(boolean hasExisting, int existingColor, List<int[]> dyes) {
        int sumR = 0, sumG = 0, sumB = 0, sumPmax = 0, n = 0;

        if (hasExisting) {
            // Vanilla round-trips the existing color through float/int to match its implementation.
            float f0 = ((existingColor >> 16) & 0xFF) / 255f;
            float f1 = ((existingColor >>  8) & 0xFF) / 255f;
            float f2 = ( existingColor        & 0xFF) / 255f;
            sumPmax += (int)(Math.max(f0, Math.max(f1, f2)) * 255f);
            sumR    += (int)(f0 * 255f);
            sumG    += (int)(f1 * 255f);
            sumB    += (int)(f2 * 255f);
            n++;
        }

        for (int[] c : dyes) {
            sumR    += c[0];
            sumG    += c[1];
            sumB    += c[2];
            sumPmax += Math.max(c[0], Math.max(c[1], c[2]));
            n++;
        }

        int avgR    = sumR    / n;
        int avgG    = sumG    / n;
        int avgB    = sumB    / n;
        float avgMax = (float) sumPmax / n;
        float maxAvg = (float) Math.max(avgR, Math.max(avgG, avgB));
        float factor = avgMax / maxAvg;

        int outR = (int)(avgR * factor);
        int outG = (int)(avgG * factor);
        int outB = (int)(avgB * factor);
        return (outR << 16) | (outG << 8) | outB;
    }
}
