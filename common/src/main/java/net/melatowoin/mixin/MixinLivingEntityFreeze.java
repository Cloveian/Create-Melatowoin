package net.melatowoin.mixin;

import net.melatowoin.client.AccessoriesSlotHelper;
import net.melatowoin.registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Grants freeze immunity to Players wearing Toe Beans in the Accessories shoes
 * slot. The feet-armor-slot case is handled by adding melatowoin:toe_beans to
 * the minecraft:freeze_immune_wearables item tag — vanilla canFreeze() already
 * consults that tag.
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntityFreeze {

    @Inject(method = "canFreeze", at = @At("RETURN"), cancellable = true)
    private void melatowoin$toeBeansAccessoryFreezeImmunity(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return; // already immune for some other reason
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;

        ItemStack acc = AccessoriesSlotHelper.findToeBeansInAccessories.apply(player);
        if (acc.is(ModItems.TOE_BEANS.get())) {
            cir.setReturnValue(false);
        }
    }
}
