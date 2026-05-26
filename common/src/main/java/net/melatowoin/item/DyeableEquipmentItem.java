package net.melatowoin.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Base class for cat-themed equipment that can be dyed in the crafting table.
 * Left/right dyes in the crafting grid set the main (fur) color.
 * Above/below dyes set the accent (paw pad / inner ear) color.
 * Multiple dyes on the same side are mixed using the Java Edition leather-armor formula.
 */
public class DyeableEquipmentItem extends ArmorItem {

    public enum EquipType { CAT_EARS, TAIL, PAWS, TOE_BEANS }

    public static final int DEFAULT_MAIN_COLOR   = 0xFFFFFF;
    public static final int DEFAULT_ACCENT_COLOR = 0xFFC3FA;

    private static final String NBT_ROOT        = "MelatowoinEquip";
    private static final String TAG_MAIN        = "MainColor";
    private static final String TAG_ACCENT      = "AccentColor";
    private static final String TAG_MAIN_DYED   = "MainDyed";
    private static final String TAG_ACCENT_DYED = "AccentDyed";

    private final EquipType equipType;

    public DyeableEquipmentItem(ArmorItem.Type armorType, EquipType equipType, Properties properties) {
        super(CatEarsArmorMaterial.CAT_EARS, armorType, properties);
        this.equipType = equipType;
    }

    public EquipType getEquipType() { return equipType; }

    // ---- Color NBT helpers ----

    public int getMainColor(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(NBT_ROOT);
        return (tag != null && tag.contains(TAG_MAIN)) ? tag.getInt(TAG_MAIN) : DEFAULT_MAIN_COLOR;
    }

    public int getAccentColor(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(NBT_ROOT);
        return (tag != null && tag.contains(TAG_ACCENT)) ? tag.getInt(TAG_ACCENT) : DEFAULT_ACCENT_COLOR;
    }

    public void setMainColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement(NBT_ROOT).putInt(TAG_MAIN, color);
    }

    public void setAccentColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement(NBT_ROOT).putInt(TAG_ACCENT, color);
    }

    /** Whether the main color has been explicitly dyed (used to include it in re-dye mixing). */
    public boolean isMainDyed(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(NBT_ROOT);
        return tag != null && tag.getBoolean(TAG_MAIN_DYED);
    }

    /** Whether the accent color has been explicitly dyed. */
    public boolean isAccentDyed(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(NBT_ROOT);
        return tag != null && tag.getBoolean(TAG_ACCENT_DYED);
    }

    public void setMainDyed(ItemStack stack, boolean dyed) {
        stack.getOrCreateTagElement(NBT_ROOT).putBoolean(TAG_MAIN_DYED, dyed);
    }

    public void setAccentDyed(ItemStack stack, boolean dyed) {
        stack.getOrCreateTagElement(NBT_ROOT).putBoolean(TAG_ACCENT_DYED, dyed);
    }

    // ---- Entity texture ResourceLocations for two-pass rendering ----

    /** Grayscale texture covering main-color areas; accent regions are transparent. */
    public ResourceLocation getEntityLayer1() {
        String name = switch (equipType) {
            case CAT_EARS           -> "ears";
            case TAIL               -> "tail";
            case PAWS, TOE_BEANS   -> "paws";
        };
        return new ResourceLocation("melatowoin", "textures/entities/equipment/" + name + "_layer1.png");
    }

    /** Grayscale texture covering accent-color areas; main regions are transparent. */
    public ResourceLocation getEntityLayer2() {
        String name = switch (equipType) {
            case CAT_EARS           -> "ears";
            case TAIL               -> "tail";
            case PAWS, TOE_BEANS   -> "paws";
        };
        return new ResourceLocation("melatowoin", "textures/entities/equipment/" + name + "_layer2.png");
    }

    // ---- Forge armor texture override ----
    // All pieces return transparent.png so vanilla HumanoidArmorLayer renders nothing.
    // Our custom render layers (CatEarsLayer / FabricCatEarsRenderer) handle everything.
    public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity,
                                   EquipmentSlot slot, String type) {
        return "melatowoin:textures/models/armor/transparent.png";
    }

    // ---- Forge enchantment override ----
    // Allows slot-specific armor enchants (Respiration, Feather Falling, etc.) in addition to
    // the generic armor enchants that already work via instanceof ArmorItem.
    public boolean canApplyAtEnchantingTable(ItemStack stack,
            net.minecraft.world.item.enchantment.Enchantment enchantment) {
        net.minecraft.world.item.enchantment.EnchantmentCategory cat = enchantment.category;
        if (cat == net.minecraft.world.item.enchantment.EnchantmentCategory.ARMOR
                || cat == net.minecraft.world.item.enchantment.EnchantmentCategory.BREAKABLE
                || cat == net.minecraft.world.item.enchantment.EnchantmentCategory.VANISHABLE) {
            return true;
        }
        return cat == slotCategory();
    }

    public net.minecraft.world.item.enchantment.EnchantmentCategory slotCategory() {
        return switch (this.getType()) {
            case HELMET     -> net.minecraft.world.item.enchantment.EnchantmentCategory.ARMOR_HEAD;
            case CHESTPLATE -> net.minecraft.world.item.enchantment.EnchantmentCategory.ARMOR_CHEST;
            case LEGGINGS   -> net.minecraft.world.item.enchantment.EnchantmentCategory.ARMOR_LEGS;
            case BOOTS      -> net.minecraft.world.item.enchantment.EnchantmentCategory.ARMOR_FEET;
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (flag.isAdvanced()) {
            int main   = getMainColor(stack);
            int accent = getAccentColor(stack);
            tooltip.add(Component.literal(String.format("§8Main: #%06X  Accent: #%06X", main, accent)));
        }
    }

    // ---- Full-set helper ----

    public static boolean isFullSetEquipped(net.minecraft.world.entity.LivingEntity entity) {
        return isType(entity.getItemBySlot(EquipmentSlot.HEAD),  EquipType.CAT_EARS)
            && isType(entity.getItemBySlot(EquipmentSlot.CHEST), EquipType.PAWS)
            && isType(entity.getItemBySlot(EquipmentSlot.LEGS),  EquipType.TAIL)
            && isType(entity.getItemBySlot(EquipmentSlot.FEET),  EquipType.TOE_BEANS);
    }

    public static boolean isType(ItemStack stack, EquipType type) {
        return stack.getItem() instanceof DyeableEquipmentItem d && d.equipType == type;
    }

    /** Returns the DyeColor's packed RGB int (firework color). */
    public static int colorOf(DyeColor dye) {
        return dye.getFireworkColor();
    }
}
