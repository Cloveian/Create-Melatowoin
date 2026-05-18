package net.melatowoin.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MAIN_TAB =
            TABS.register("main", () -> CreativeTabRegistry.create(
                    Component.translatable("itemGroup.melatowoin.main"),
                    () -> new ItemStack(ModItems.BLACKCATEARS.get())
            ));

    public static void register() {
        TABS.register();

        // Populate the tab with all mod items
        CreativeTabRegistry.modify(MAIN_TAB, (flags, output, operatorEnabled) -> {
            // Simple items
            output.accept(ModItems.FUR.get());
            output.accept(ModItems.STRINGS.get());
            output.accept(ModItems.DUST.get());
            output.accept(ModItems.PLASTIC.get());
            output.accept(ModItems.FABRICS.get());
            output.accept(ModItems.FABRIC.get());
            output.accept(ModItems.CA.get());
            output.accept(ModItems.PUREFURRYNESS.get());
            output.accept(ModItems.UNPUREFURRYNESS.get());

            // Food items
            output.accept(ModItems.FURRYCORE.get());
            output.accept(ModItems.CYANPILL.get());
            output.accept(ModItems.ORANGPIL.get());

            // Throwables
            output.accept(ModItems.CYANABLE.get());
            output.accept(ModItems.ORANGEABLE.get());

            // Armor — all 16 dye colors
            output.accept(ModItems.BLACKCATEARS.get());
            output.accept(ModItems.BLACKTAIL.get());
            output.accept(ModItems.BLUECATEARS.get());
            output.accept(ModItems.BLUETAIL.get());
            output.accept(ModItems.BROWNCATEARS.get());
            output.accept(ModItems.BROWNTAIL.get());
            output.accept(ModItems.CYANCATEARS.get());
            output.accept(ModItems.CYANTAIL.get());
            output.accept(ModItems.GRAYCATEARS.get());
            output.accept(ModItems.GRAYTAIL.get());
            output.accept(ModItems.GREENCATEARS.get());
            output.accept(ModItems.GREENTAIL.get());
            output.accept(ModItems.LIGHTBLUECATEARS.get());
            output.accept(ModItems.LIGHTBLUETAIL.get());
            output.accept(ModItems.LIGHTGRAYCATEARS.get());
            output.accept(ModItems.LIGHTGRAYTAIL.get());
            output.accept(ModItems.LIMECATEARS.get());
            output.accept(ModItems.LIMETAIL.get());
            output.accept(ModItems.MAGENTACATEARS.get());
            output.accept(ModItems.MAGENTATAIL.get());
            output.accept(ModItems.ORANGECATEARS.get());
            output.accept(ModItems.ORANGETAIL.get());
            output.accept(ModItems.PINKCATEARS.get());
            output.accept(ModItems.PINKTAIL.get());
            output.accept(ModItems.PURPLECATEARS.get());
            output.accept(ModItems.PURPLETAIL.get());
            output.accept(ModItems.REDCATEARS.get());
            output.accept(ModItems.REDTAIL.get());
            output.accept(ModItems.WHITECATEARS.get());
            output.accept(ModItems.WHITETAIL.get());
            output.accept(ModItems.YELLOWCATEARS.get());
            output.accept(ModItems.YELLOWTAIL.get());

            // Buckets (only added if platform has registered them)
            if (ModItems.ACETONE_BUCKET != null) output.accept(ModItems.ACETONE_BUCKET.get());
            if (ModItems.BLEACH_BUCKET != null) output.accept(ModItems.BLEACH_BUCKET.get());
            if (ModItems.CHLOROFORM_BUCKET != null) output.accept(ModItems.CHLOROFORM_BUCKET.get());
            if (ModItems.VINEGAR_BUCKET != null) output.accept(ModItems.VINEGAR_BUCKET.get());
        });
    }
}
