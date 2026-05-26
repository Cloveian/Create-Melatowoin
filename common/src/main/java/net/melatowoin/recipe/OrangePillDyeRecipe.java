package net.melatowoin.recipe;

import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.item.OrangeColorData;
import net.melatowoin.registry.ModItems;
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

public class OrangePillDyeRecipe extends CustomRecipe {

    public OrangePillDyeRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return findPillSlot(container) >= 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        int pillSlot = findPillSlot(container);
        if (pillSlot < 0) return ItemStack.EMPTY;

        int w = container.getWidth();
        int pillRow = pillSlot / w, pillCol = pillSlot % w;

        List<int[]> mainDyes   = new ArrayList<>();
        List<int[]> accentDyes = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty() || i == pillSlot) continue;
            if (!(stack.getItem() instanceof DyeItem dye)) continue;

            float[] rgb = dye.getDyeColor().getTextureDiffuseColors();
            int[] c = {
                (int)(rgb[0] * 255f),
                (int)(rgb[1] * 255f),
                (int)(rgb[2] * 255f)
            };

            int dr = i / w;
            if (dr == pillRow) mainDyes.add(c);
            else               accentDyes.add(c);
        }

        if (mainDyes.isEmpty() && accentDyes.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = container.getItem(pillSlot).copy();
        OrangeColorData existing = OrangeColorData.fromStack(result);
        boolean hasExisting = existing != null;

        int curMain   = hasExisting ? existing.earMain()   : DyeableEquipmentItem.DEFAULT_MAIN_COLOR;
        int curAccent = hasExisting ? existing.earAccent() : DyeableEquipmentItem.DEFAULT_ACCENT_COLOR;

        int newMain   = mainDyes.isEmpty()   ? curMain   : DyeableEquipmentDyeRecipe.mix(hasExisting, curMain,   mainDyes);
        int newAccent = accentDyes.isEmpty() ? curAccent : DyeableEquipmentDyeRecipe.mix(hasExisting, curAccent, accentDyes);

        new OrangeColorData(
                newMain, newAccent,
                newMain, newAccent,
                newMain, newAccent,
                newMain, newAccent
        ).writeTo(result);

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 || height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ORANGE_PILL_DYE.get();
    }

    private static int findPillSlot(CraftingContainer container) {
        int w = container.getWidth();
        int pillSlot = -1;
        boolean hasDye = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(ModItems.ORANGE_PILL.get())) {
                if (pillSlot != -1) return -1;
                pillSlot = i;
            } else if (!(stack.getItem() instanceof DyeItem)) {
                return -1;
            }
        }
        if (pillSlot < 0) return -1;

        int pillRow = pillSlot / w, pillCol = pillSlot % w;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty() || i == pillSlot) continue;
            if (!(stack.getItem() instanceof DyeItem)) return -1;

            int dr = i / w, dc = i % w;
            boolean leftRight  = (dr == pillRow   && Math.abs(dc - pillCol)  == 1);
            boolean aboveBelow = (dc == pillCol   && Math.abs(dr - pillRow)  == 1);
            if (!leftRight && !aboveBelow) return -1;
            hasDye = true;
        }
        return hasDye ? pillSlot : -1;
    }
}
