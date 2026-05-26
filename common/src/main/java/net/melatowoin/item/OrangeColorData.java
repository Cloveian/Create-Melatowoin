package net.melatowoin.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record OrangeColorData(
        int earMain,  int earAccent,
        int tailMain, int tailAccent,
        int pawsMain, int pawsAccent,
        int toeMain,  int toeAccent
) {
    private static final String TAG = "MelatoColors";

    public static @Nullable OrangeColorData fromStack(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG)) return null;
        CompoundTag t = stack.getTag().getCompound(TAG);
        return new OrangeColorData(
                t.getInt("EM"), t.getInt("EA"),
                t.getInt("TM"), t.getInt("TA"),
                t.getInt("PM"), t.getInt("PA"),
                t.getInt("BM"), t.getInt("BA")
        );
    }

    public void writeTo(ItemStack stack) {
        CompoundTag t = new CompoundTag();
        t.putInt("EM", earMain);   t.putInt("EA", earAccent);
        t.putInt("TM", tailMain);  t.putInt("TA", tailAccent);
        t.putInt("PM", pawsMain);  t.putInt("PA", pawsAccent);
        t.putInt("BM", toeMain);   t.putInt("BA", toeAccent);
        stack.getOrCreateTag().put(TAG, t);
    }

    public static OrangeColorData fromCosmetics(ItemStack ears, ItemStack tail,
                                                 ItemStack paws, ItemStack toe) {
        DyeableEquipmentItem e  = (DyeableEquipmentItem) ears.getItem();
        DyeableEquipmentItem ta = (DyeableEquipmentItem) tail.getItem();
        DyeableEquipmentItem p  = (DyeableEquipmentItem) paws.getItem();
        DyeableEquipmentItem to = (DyeableEquipmentItem) toe.getItem();
        return new OrangeColorData(
                e.getMainColor(ears),   e.getAccentColor(ears),
                ta.getMainColor(tail),  ta.getAccentColor(tail),
                p.getMainColor(paws),   p.getAccentColor(paws),
                to.getMainColor(toe),   to.getAccentColor(toe)
        );
    }
}
