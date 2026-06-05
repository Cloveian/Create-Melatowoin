package net.melatowoin.mixin;

import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.item.DyeableEquipmentItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets Accessories-slot cat pieces count toward Create: New Age's radiation
 * protection check. Vanilla {@code NuclearUtil.isResistant} requires every
 * one of the four armor slots to hold a {@code create_new_age:hazmat_suit}
 * tagged item. With this mixin, any of those armor slots may instead be
 * "covered" by the corresponding cat piece in its Accessories slot:
 *
 *   HEAD  ←→ Accessories hat   (Cat Ears)
 *   CHEST ←→ Accessories hand  (Paws)
 *   LEGS  ←→ Accessories belt  (Tail)
 *   FEET  ←→ Accessories shoes (Toe Beans)
 *
 * Targeted by string so the mixin is silently skipped if Create: New Age is
 * not installed.
 */
@Pseudo
@Mixin(targets = "org.antarcticgardens.cna.content.nuclear.NuclearUtil", remap = false)
public class MixinNuclearUtil {

    private static final TagKey<Item> HAZMAT_SUIT =
            TagKey.create(Registries.ITEM, new ResourceLocation("create_new_age", "hazmat_suit"));

    @Inject(method = "isResistant", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void melatowoin$accessoriesCount(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return; // vanilla path handles this

        if (melatowoin$slotCovered(player, EquipmentSlot.HEAD,  DyeableEquipmentItem.EquipType.CAT_EARS)
         && melatowoin$slotCovered(player, EquipmentSlot.CHEST, DyeableEquipmentItem.EquipType.PAWS)
         && melatowoin$slotCovered(player, EquipmentSlot.LEGS,  DyeableEquipmentItem.EquipType.TAIL)
         && melatowoin$slotCovered(player, EquipmentSlot.FEET,  DyeableEquipmentItem.EquipType.TOE_BEANS)) {
            cir.setReturnValue(true);
        }
        // else: fall through to vanilla check — if every armor slot is already
        // hazmat-tagged the original method will still return true.
    }

    private static boolean melatowoin$slotCovered(Player player, EquipmentSlot slot,
                                                  DyeableEquipmentItem.EquipType type) {
        ItemStack armor = player.getItemBySlot(slot);
        if (armor.is(HAZMAT_SUIT)) return true;
        ItemStack acc = switch (type) {
            case CAT_EARS  -> AccessoriesSlotHelper.findCatEarsInAccessories.apply(player);
            case TAIL      -> AccessoriesSlotHelper.findTailInAccessories.apply(player);
            case PAWS      -> AccessoriesSlotHelper.findPawsInAccessories.apply(player);
            case TOE_BEANS -> AccessoriesSlotHelper.findToeBeansInAccessories.apply(player);
        };
        return DyeableEquipmentItem.isType(acc, type);
    }
}
