package net.melatowoin.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
 * Left/right of item in crafting grid sets the main (fur) color.
 * Above/below of item sets the accent (paw pad) color.
 */
public class DyeableEquipmentItem extends ArmorItem {

    public enum EquipType { CAT_EARS, TAIL, PAWS, TOE_BEANS }

    public static final int DEFAULT_MAIN_COLOR   = 0xFFFFFF; // white — shown as-is without tint
    public static final int DEFAULT_ACCENT_COLOR = 0xFFB6C1; // light pink paw pads

    private static final String NBT_ROOT   = "MelatowoinEquip";
    private static final String TAG_MAIN   = "MainColor";
    private static final String TAG_ACCENT = "AccentColor";

    private final EquipType equipType;

    public DyeableEquipmentItem(ArmorItem.Type armorType, EquipType equipType, Properties properties) {
        super(CatEarsArmorMaterial.CAT_EARS, armorType, properties);
        this.equipType = equipType;
    }

    public EquipType getEquipType() {
        return equipType;
    }

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

    // ---- Entity texture for custom render layers ----

    /** Texture used by CatEarsLayer / FabricCatEarsRenderer for cat ears geometry. */
    public String getEarTexture() {
        return "melatowoin:textures/entities/white_ears.png";
    }

    /** Texture used by CatEarsLayer / FabricCatEarsRenderer for tail geometry. */
    public String getTailTexture() {
        return "melatowoin:textures/entities/white_tail.png";
    }

    // ---- Forge armor texture override ----
    // Returning transparent.png hides the vanilla HumanoidArmorLayer so our
    // custom render layers (CatEarsLayer on Forge, FabricCatEarsRenderer on Fabric)
    // are the sole renderers for cat_ears and tail.
    // For toe_beans we return the actual boot texture so vanilla handles it.
    public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity,
                                   EquipmentSlot slot, String type) {
        if (equipType == EquipType.TOE_BEANS) {
            return type == null
                    ? "melatowoin:textures/models/armor/toe_beans_layer_1.png"
                    : "melatowoin:textures/models/armor/toe_beans_layer_2.png";
        }
        return "melatowoin:textures/models/armor/transparent.png";
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

    // ---- Helper: check whether a full catgirl set is equipped ----
    public static boolean isFullSetEquipped(net.minecraft.world.entity.LivingEntity entity) {
        return isType(entity.getItemBySlot(EquipmentSlot.HEAD),  EquipType.CAT_EARS)
            && isType(entity.getItemBySlot(EquipmentSlot.CHEST), EquipType.PAWS)
            && isType(entity.getItemBySlot(EquipmentSlot.LEGS),  EquipType.TAIL)
            && isType(entity.getItemBySlot(EquipmentSlot.FEET),  EquipType.TOE_BEANS);
    }

    public static boolean isType(ItemStack stack, EquipType type) {
        return stack.getItem() instanceof DyeableEquipmentItem d && d.equipType == type;
    }

    /** Returns the DyeColor's packed RGB int. */
    public static int colorOf(DyeColor dye) {
        return dye.getFireworkColor();
    }
}
