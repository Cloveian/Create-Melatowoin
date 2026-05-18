package net.melatowoin.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.item.CatEarsArmorItem;
import net.melatowoin.item.CyanPillItem;
import net.melatowoin.item.CyanSauceItem;
import net.melatowoin.item.OrangeSauceItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.ITEM);

    // ---- Simple items ----

    public static final RegistrySupplier<Item> FUR =
            ITEMS.register("fur", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistrySupplier<Item> STRINGS =
            ITEMS.register("strings", () -> new Item(new Item.Properties().stacksTo(64)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 weird ball of strings"));
                }
            });

    public static final RegistrySupplier<Item> DUST =
            ITEMS.register("dust", () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 dusty"));
                }
            });

    public static final RegistrySupplier<Item> PLASTIC =
            ITEMS.register("plastic", () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistrySupplier<Item> FABRICS =
            ITEMS.register("fabrics", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> FABRIC =
            ITEMS.register("fabric", () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 better than wool"));
                }
            });

    public static final RegistrySupplier<Item> CA =
            ITEMS.register("ca", () -> new Item(new Item.Properties().stacksTo(16)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 We need to cook"));
                }
            });

    public static final RegistrySupplier<Item> PUREFURRYNESS =
            ITEMS.register("purefurryness", () -> new Item(new Item.Properties().stacksTo(64)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 \"95% pure\""));
                }
            });

    public static final RegistrySupplier<Item> UNPUREFURRYNESS =
            ITEMS.register("unpurefurryness", () -> new Item(new Item.Properties().stacksTo(64)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 you will regret going this path."));
                }
            });

    // ---- Food items ----

    /** Furrycore — edible but 0 nutrition, no special effect */
    public static final RegistrySupplier<Item> FURRYCORE =
            ITEMS.register("furrycore", () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .food(new FoodProperties.Builder()
                                    .nutrition(0).saturationMod(0).alwaysEat().build())) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 what"));
                }
            });

    /** Cyan pill — applies Eepy for 200 ticks on finish-using */
    public static final RegistrySupplier<Item> CYANPILL =
            ITEMS.register("cyanpill", () -> new CyanPillItem(
                    new Item.Properties()
                            .stacksTo(64)
                            .food(new FoodProperties.Builder()
                                    .nutrition(0).saturationMod(0).alwaysEat().build())));

    /** Orange pill — edible, no special effect */
    public static final RegistrySupplier<Item> ORANGPIL =
            ITEMS.register("orangpil", () -> new Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .food(new FoodProperties.Builder()
                                    .nutrition(0).saturationMod(0).alwaysEat().build())));

    // ---- Throwable sauce items ----

    public static final RegistrySupplier<Item> CYANABLE =
            ITEMS.register("cyanable", () -> new CyanSauceItem(new Item.Properties().stacksTo(64)));

    public static final RegistrySupplier<Item> ORANGEABLE =
            ITEMS.register("orangeable", () -> new OrangeSauceItem(new Item.Properties().stacksTo(64)));

    // ---- Bucket items for fluids ----
    // These forward to platform-specific BucketItem registrations; the bucket items
    // need a Fluid reference which is available after platform fluid registration.
    // We store them here so they're accessible from common code (e.g. creative tab).
    // Platform modules call ModItems.registerBuckets() after fluids are ready.

    public static RegistrySupplier<Item> ACETONE_BUCKET;
    public static RegistrySupplier<Item> BLEACH_BUCKET;
    public static RegistrySupplier<Item> CHLOROFORM_BUCKET;
    public static RegistrySupplier<Item> VINEGAR_BUCKET;

    // ---- Armor items ----

    // Black cat ears
    public static final RegistrySupplier<Item> BLACKCATEARS =
            ITEMS.register("blackcatears", () ->
                    new CatEarsArmorItem.Helmet("black", "§8 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> BLACKTAIL =
            ITEMS.register("blacktail", () ->
                    new CatEarsArmorItem.Leggings("black", "§8 fluffy tail", new Item.Properties()));

    // Blue cat ears
    public static final RegistrySupplier<Item> BLUECATEARS =
            ITEMS.register("bluecatears", () ->
                    new CatEarsArmorItem.Helmet("blue", "§9 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> BLUETAIL =
            ITEMS.register("bluetail", () ->
                    new CatEarsArmorItem.Leggings("blue", "§9 fluffy tail", new Item.Properties()));

    // Green cat ears
    public static final RegistrySupplier<Item> GREENCATEARS =
            ITEMS.register("greencatears", () ->
                    new CatEarsArmorItem.Helmet("green", "§2 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> GREENTAIL =
            ITEMS.register("greentail", () ->
                    new CatEarsArmorItem.Leggings("green", "§2 fluffy tail", new Item.Properties()));

    // Orange cat ears
    public static final RegistrySupplier<Item> ORANGECATEARS =
            ITEMS.register("orangecatears", () ->
                    new CatEarsArmorItem.Helmet("orange", "§6 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> ORANGETAIL =
            ITEMS.register("orangetail", () ->
                    new CatEarsArmorItem.Leggings("orange", "§6 fluffy tail", new Item.Properties()));

    // Red cat ears
    public static final RegistrySupplier<Item> REDCATEARS =
            ITEMS.register("redcatears", () ->
                    new CatEarsArmorItem.Helmet("red", "§4 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> REDTAIL =
            ITEMS.register("redtail", () ->
                    new CatEarsArmorItem.Leggings("red", "§4 fluffy tail", new Item.Properties()));

    // White cat ears
    public static final RegistrySupplier<Item> WHITECATEARS =
            ITEMS.register("whitecatears", () ->
                    new CatEarsArmorItem.Helmet("white", "§f Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> WHITETAIL =
            ITEMS.register("whitetail", () ->
                    new CatEarsArmorItem.Leggings("white", "§f fluffy tail", new Item.Properties()));

    // Brown cat ears
    public static final RegistrySupplier<Item> BROWNCATEARS =
            ITEMS.register("browncatears", () ->
                    new CatEarsArmorItem.Helmet("brown", "§6 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> BROWNTAIL =
            ITEMS.register("browntail", () ->
                    new CatEarsArmorItem.Leggings("brown", "§6 fluffy tail", new Item.Properties()));

    // Cyan cat ears
    public static final RegistrySupplier<Item> CYANCATEARS =
            ITEMS.register("cyancatears", () ->
                    new CatEarsArmorItem.Helmet("cyan", "§b Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> CYANTAIL =
            ITEMS.register("cyantail", () ->
                    new CatEarsArmorItem.Leggings("cyan", "§b fluffy tail", new Item.Properties()));

    // Gray cat ears
    public static final RegistrySupplier<Item> GRAYCATEARS =
            ITEMS.register("graycatears", () ->
                    new CatEarsArmorItem.Helmet("gray", "§8 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> GRAYTAIL =
            ITEMS.register("graytail", () ->
                    new CatEarsArmorItem.Leggings("gray", "§8 fluffy tail", new Item.Properties()));

    // Light blue cat ears
    public static final RegistrySupplier<Item> LIGHTBLUECATEARS =
            ITEMS.register("lightbluecatears", () ->
                    new CatEarsArmorItem.Helmet("light_blue", "§3 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> LIGHTBLUETAIL =
            ITEMS.register("lightbluetail", () ->
                    new CatEarsArmorItem.Leggings("light_blue", "§3 fluffy tail", new Item.Properties()));

    // Light gray cat ears
    public static final RegistrySupplier<Item> LIGHTGRAYCATEARS =
            ITEMS.register("lightgraycatears", () ->
                    new CatEarsArmorItem.Helmet("light_gray", "§7 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> LIGHTGRAYTAIL =
            ITEMS.register("lightgraytail", () ->
                    new CatEarsArmorItem.Leggings("light_gray", "§7 fluffy tail", new Item.Properties()));

    // Lime cat ears
    public static final RegistrySupplier<Item> LIMECATEARS =
            ITEMS.register("limecatears", () ->
                    new CatEarsArmorItem.Helmet("lime", "§a Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> LIMETAIL =
            ITEMS.register("limetail", () ->
                    new CatEarsArmorItem.Leggings("lime", "§a fluffy tail", new Item.Properties()));

    // Magenta cat ears
    public static final RegistrySupplier<Item> MAGENTACATEARS =
            ITEMS.register("magentacatears", () ->
                    new CatEarsArmorItem.Helmet("magenta", "§d Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> MAGENTATAIL =
            ITEMS.register("magentatail", () ->
                    new CatEarsArmorItem.Leggings("magenta", "§d fluffy tail", new Item.Properties()));

    // Pink cat ears
    public static final RegistrySupplier<Item> PINKCATEARS =
            ITEMS.register("pinkcatears", () ->
                    new CatEarsArmorItem.Helmet("pink", "§d Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> PINKTAIL =
            ITEMS.register("pinktail", () ->
                    new CatEarsArmorItem.Leggings("pink", "§d fluffy tail", new Item.Properties()));

    // Purple cat ears
    public static final RegistrySupplier<Item> PURPLECATEARS =
            ITEMS.register("purplecatears", () ->
                    new CatEarsArmorItem.Helmet("purple", "§5 Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> PURPLETAIL =
            ITEMS.register("purpletail", () ->
                    new CatEarsArmorItem.Leggings("purple", "§5 fluffy tail", new Item.Properties()));

    // Yellow cat ears
    public static final RegistrySupplier<Item> YELLOWCATEARS =
            ITEMS.register("yellowcatears", () ->
                    new CatEarsArmorItem.Helmet("yellow", "§e Nyaa~", new Item.Properties()));
    public static final RegistrySupplier<Item> YELLOWTAIL =
            ITEMS.register("yellowtail", () ->
                    new CatEarsArmorItem.Leggings("yellow", "§e fluffy tail", new Item.Properties()));

    public static void register() {
        ITEMS.register();
    }
}
