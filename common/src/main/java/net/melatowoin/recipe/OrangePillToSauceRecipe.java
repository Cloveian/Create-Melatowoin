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

public class OrangePillToSauceRecipe extends ShapelessRecipe {

    public OrangePillToSauceRecipe(ResourceLocation id, CraftingBookCategory category,
                                    NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, "", category, result, ingredients);
    }

    @Override
    public ItemStack assemble(CraftingContainer c, RegistryAccess access) {
        ItemStack pill = ItemStack.EMPTY;
        for (int i = 0; i < c.getContainerSize(); i++) {
            ItemStack s = c.getItem(i);
            if (s.is(ModItems.ORANGE_PILL.get())) { pill = s; break; }
        }
        ItemStack sauce = new ItemStack(ModItems.ORANGEABLE.get());
        if (pill.hasTag()) sauce.setTag(pill.getTag().copy());
        return sauce;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ORANGE_PILL_TO_SAUCE.get();
    }
}
