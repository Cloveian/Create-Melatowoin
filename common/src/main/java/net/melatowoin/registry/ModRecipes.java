package net.melatowoin.registry;

import com.google.gson.JsonObject;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.recipe.DyeableEquipmentDyeRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeSerializer<DyeableEquipmentDyeRecipe>> DYEABLE_EQUIPMENT_DYE =
            RECIPE_SERIALIZERS.register("dyeable_equipment_dye", DyeSerializer::new);

    public static void register() {
        RECIPE_SERIALIZERS.register();
    }

    private static class DyeSerializer implements RecipeSerializer<DyeableEquipmentDyeRecipe> {

        @Override
        public DyeableEquipmentDyeRecipe fromJson(ResourceLocation id, JsonObject json) {
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(
                    GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            return new DyeableEquipmentDyeRecipe(id, category);
        }

        @Override
        public DyeableEquipmentDyeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            return new DyeableEquipmentDyeRecipe(id, category);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DyeableEquipmentDyeRecipe recipe) {
            buf.writeEnum(recipe.category());
        }
    }
}
