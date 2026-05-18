package net.melatowoin.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.item.CyanPillItem;
import net.melatowoin.item.CyanSauceItem;
import net.melatowoin.item.DyeableEquipmentItem;
import net.melatowoin.item.OrangeSauceItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.ITEM);

    // ---- Simple crafting materials ----

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

    // ---- Intermediate wearable-crafting items ----

    /** Socks — crafted like boots using fabric; processed into Toe Beans via Create. */
    public static final RegistrySupplier<Item> SOCKS =
            ITEMS.register("socks", () -> new Item(new Item.Properties().stacksTo(16)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 soft"));
                }
            });

    /** Mittens — crafted from fabric; processed into Paws via Create. */
    public static final RegistrySupplier<Item> MITTENS =
            ITEMS.register("mittens", () -> new Item(new Item.Properties().stacksTo(16)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 fluffy"));
                }
            });

    /** Headband — crafted from fabric + iron nuggets; processed into Cat Ears via Create. */
    public static final RegistrySupplier<Item> HEADBAND =
            ITEMS.register("headband", () -> new Item(new Item.Properties().stacksTo(16)) {
                @Override
                public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                            java.util.List<Component> list, TooltipFlag flag) {
                    super.appendHoverText(stack, level, list, flag);
                    list.add(Component.literal("§8 holds ears"));
                }
            });

    // ---- Dyeable wearable items ----

    /** Cat Ears — helmet slot + Accessories hat slot. Dyeable (main = fur, accent = inner ear). */
    public static final RegistrySupplier<Item> CAT_EARS =
            ITEMS.register("cat_ears", () ->
                    new DyeableEquipmentItem(ArmorItem.Type.HELMET, DyeableEquipmentItem.EquipType.CAT_EARS,
                            new Item.Properties()));

    /** Tail — leggings slot + Accessories belt slot. Dyeable. */
    public static final RegistrySupplier<Item> TAIL =
            ITEMS.register("tail", () ->
                    new DyeableEquipmentItem(ArmorItem.Type.LEGGINGS, DyeableEquipmentItem.EquipType.TAIL,
                            new Item.Properties()));

    /** Paws — chestplate slot + Accessories gloves slot. Dyeable. Makes chest opening silent. */
    public static final RegistrySupplier<Item> PAWS =
            ITEMS.register("paws", () ->
                    new DyeableEquipmentItem(ArmorItem.Type.CHESTPLATE, DyeableEquipmentItem.EquipType.PAWS,
                            new Item.Properties()));

    /** Toe Beans — boots slot + Accessories feet slot. Dyeable. Silences footsteps. */
    public static final RegistrySupplier<Item> TOE_BEANS =
            ITEMS.register("toe_beans", () ->
                    new DyeableEquipmentItem(ArmorItem.Type.BOOTS, DyeableEquipmentItem.EquipType.TOE_BEANS,
                            new Item.Properties()));

    // ---- Food items ----

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

    public static final RegistrySupplier<Item> CYANPILL =
            ITEMS.register("cyanpill", () -> new CyanPillItem(
                    new Item.Properties()
                            .stacksTo(64)
                            .food(new FoodProperties.Builder()
                                    .nutrition(0).saturationMod(0).alwaysEat().build())));

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

    // ---- Bucket items (registered by platform modules after fluid registration) ----

    public static RegistrySupplier<Item> ACETONE_BUCKET;
    public static RegistrySupplier<Item> BLEACH_BUCKET;
    public static RegistrySupplier<Item> CHLOROFORM_BUCKET;
    public static RegistrySupplier<Item> VINEGAR_BUCKET;

    public static void register() {
        ITEMS.register();
    }
}
