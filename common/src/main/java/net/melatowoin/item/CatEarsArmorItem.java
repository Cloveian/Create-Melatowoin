package net.melatowoin.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Base class for cat ears armor items. Subclasses represent specific colors.
 * The actual visual model is a custom layer (cat ears / tail), not the standard armor texture.
 */
public abstract class CatEarsArmorItem extends ArmorItem {

    private final String colorName;
    private final String tooltipLore;

    public CatEarsArmorItem(Type type, String colorName, String tooltipLore, Properties properties) {
        super(CatEarsArmorMaterial.CAT_EARS, type, properties);
        this.colorName = colorName;
        this.tooltipLore = tooltipLore;
    }

    public String getColorName() {
        return colorName;
    }

    /**
     * Returns the resource path to the ear texture for this color.
     * Convention: "melatowoin:textures/models/armor/<color>_catears.png"
     */
    public String getEarTexture() {
        return "melatowoin:textures/entities/" + colorName.toLowerCase() + "_ears.png";
    }

    public String getTailTexture() {
        return "melatowoin:textures/entities/" + colorName.toLowerCase() + "_tail.png";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.literal(tooltipLore));
    }

    // ---- Concrete subclasses ----

    public static class Helmet extends CatEarsArmorItem {
        public Helmet(String colorName, String lore, Properties properties) {
            super(Type.HELMET, colorName, lore, properties);
        }
    }

    public static class Leggings extends CatEarsArmorItem {
        public Leggings(String colorName, String lore, Properties properties) {
            super(Type.LEGGINGS, colorName, lore, properties);
        }
    }
}
