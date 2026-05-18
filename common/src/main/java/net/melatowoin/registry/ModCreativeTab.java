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
                    () -> new ItemStack(ModItems.CAT_EARS.get())
            ));

    public static void register() {
        TABS.register();

        CreativeTabRegistry.modify(MAIN_TAB, (flags, output, operatorEnabled) -> {
            // Crafting materials
            output.accept(ModItems.FUR.get());
            output.accept(ModItems.STRINGS.get());
            output.accept(ModItems.DUST.get());
            output.accept(ModItems.PLASTIC.get());
            output.accept(ModItems.FABRICS.get());
            output.accept(ModItems.FABRIC.get());
            output.accept(ModItems.CA.get());
            output.accept(ModItems.PUREFURRYNESS.get());
            output.accept(ModItems.UNPUREFURRYNESS.get());

            // Intermediate wearable-crafting items
            output.accept(ModItems.SOCKS.get());
            output.accept(ModItems.MITTENS.get());
            output.accept(ModItems.HEADBAND.get());

            // Wearable dyeable items
            output.accept(ModItems.CAT_EARS.get());
            output.accept(ModItems.TAIL.get());
            output.accept(ModItems.PAWS.get());
            output.accept(ModItems.TOE_BEANS.get());

            // Food
            output.accept(ModItems.FURRYCORE.get());
            output.accept(ModItems.CYANPILL.get());
            output.accept(ModItems.ORANGPIL.get());

            // Throwables
            output.accept(ModItems.CYANABLE.get());
            output.accept(ModItems.ORANGEABLE.get());

            // Buckets
            if (ModItems.ACETONE_BUCKET   != null) output.accept(ModItems.ACETONE_BUCKET.get());
            if (ModItems.BLEACH_BUCKET    != null) output.accept(ModItems.BLEACH_BUCKET.get());
            if (ModItems.CHLOROFORM_BUCKET != null) output.accept(ModItems.CHLOROFORM_BUCKET.get());
            if (ModItems.VINEGAR_BUCKET   != null) output.accept(ModItems.VINEGAR_BUCKET.get());
        });
    }
}
