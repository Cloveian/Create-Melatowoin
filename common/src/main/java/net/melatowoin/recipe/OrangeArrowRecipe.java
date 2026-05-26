package net.melatowoin.recipe;

import net.melatowoin.registry.ModItems;
import net.melatowoin.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class OrangeArrowRecipe extends ShapelessRecipe {

    public OrangeArrowRecipe(ResourceLocation id, CraftingBookCategory category,
                              NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, "", category, result, ingredients);
    }

    @Override
    public ItemStack assemble(CraftingContainer c, RegistryAccess access) {
        ItemStack sauce = ItemStack.EMPTY;
        for (int i = 0; i < c.getContainerSize(); i++) {
            ItemStack s = c.getItem(i);
            if (s.is(ModItems.ORANGEABLE.get())) { sauce = s; break; }
        }
        ItemStack arrow = new ItemStack(ModItems.ORANGE_ARROW.get());
        if (sauce.hasTag()) arrow.setTag(sauce.getTag().copy());
        return arrow;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ORANGE_ARROW.get();
    }
}
