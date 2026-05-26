package net.melatowoin.registry;

import com.google.gson.JsonObject;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.recipe.DyeableEquipmentDyeRecipe;
import net.melatowoin.recipe.OrangeArrowRecipe;
import net.melatowoin.recipe.OrangePillDyeRecipe;
import net.melatowoin.recipe.OrangePillToSauceRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeSerializer<DyeableEquipmentDyeRecipe>> DYEABLE_EQUIPMENT_DYE =
            RECIPE_SERIALIZERS.register("dyeable_equipment_dye", DyeSerializer::new);

    public static final RegistrySupplier<RecipeSerializer<OrangePillToSauceRecipe>> ORANGE_PILL_TO_SAUCE =
            RECIPE_SERIALIZERS.register("orange_pill_to_sauce", OrangePillToSauceSerializer::new);

    public static final RegistrySupplier<RecipeSerializer<OrangeArrowRecipe>> ORANGE_ARROW =
            RECIPE_SERIALIZERS.register("orange_arrow", OrangeArrowSerializer::new);

    public static final RegistrySupplier<RecipeSerializer<OrangePillDyeRecipe>> ORANGE_PILL_DYE =
            RECIPE_SERIALIZERS.register("orange_pill_dye", OrangePillDyeSerializer::new);

    public static void register() {
        RECIPE_SERIALIZERS.register();
    }

    private static class DyeSerializer implements RecipeSerializer<DyeableEquipmentDyeRecipe> {
        @Override
        public DyeableEquipmentDyeRecipe fromJson(ResourceLocation id, JsonObject json) {
            CraftingBookCategory cat = CraftingBookCategory.CODEC.byName(
                    GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new DyeableEquipmentDyeRecipe(id, cat);
        }
        @Override
        public DyeableEquipmentDyeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new DyeableEquipmentDyeRecipe(id, buf.readEnum(CraftingBookCategory.class));
        }
        @Override
        public void toNetwork(FriendlyByteBuf buf, DyeableEquipmentDyeRecipe recipe) {
            buf.writeEnum(recipe.category());
        }
    }

    private static class OrangePillToSauceSerializer implements RecipeSerializer<OrangePillToSauceRecipe> {
        @Override
        public OrangePillToSauceRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe vanilla = RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json);
            return new OrangePillToSauceRecipe(id, vanilla.category(), vanilla.getIngredients(),
                    vanilla.getResultItem(net.minecraft.core.RegistryAccess.EMPTY));
        }
        @Override
        public OrangePillToSauceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe vanilla = RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buf);
            return new OrangePillToSauceRecipe(id, vanilla.category(), vanilla.getIngredients(),
                    vanilla.getResultItem(net.minecraft.core.RegistryAccess.EMPTY));
        }
        @Override
        public void toNetwork(FriendlyByteBuf buf, OrangePillToSauceRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buf, recipe);
        }
    }

    private static class OrangePillDyeSerializer implements RecipeSerializer<OrangePillDyeRecipe> {
        @Override
        public OrangePillDyeRecipe fromJson(ResourceLocation id, JsonObject json) {
            CraftingBookCategory cat = CraftingBookCategory.CODEC.byName(
                    GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new OrangePillDyeRecipe(id, cat);
        }
        @Override
        public OrangePillDyeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new OrangePillDyeRecipe(id, buf.readEnum(CraftingBookCategory.class));
        }
        @Override
        public void toNetwork(FriendlyByteBuf buf, OrangePillDyeRecipe recipe) {
            buf.writeEnum(recipe.category());
        }
    }

    private static class OrangeArrowSerializer implements RecipeSerializer<OrangeArrowRecipe> {
        @Override
        public OrangeArrowRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe vanilla = RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json);
            return new OrangeArrowRecipe(id, vanilla.category(), vanilla.getIngredients(),
                    vanilla.getResultItem(net.minecraft.core.RegistryAccess.EMPTY));
        }
        @Override
        public OrangeArrowRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe vanilla = RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buf);
            return new OrangeArrowRecipe(id, vanilla.category(), vanilla.getIngredients(),
                    vanilla.getResultItem(net.minecraft.core.RegistryAccess.EMPTY));
        }
        @Override
        public void toNetwork(FriendlyByteBuf buf, OrangeArrowRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buf, recipe);
        }
    }
}
