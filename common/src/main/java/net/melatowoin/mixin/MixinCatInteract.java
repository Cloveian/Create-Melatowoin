package net.melatowoin.mixin;

import net.melatowoin.event.CatBrushHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public class MixinCatInteract {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void melatowoin$onBrushInteract(Player player, InteractionHand hand,
                                             CallbackInfoReturnable<InteractionResult> cir) {
        boolean consumed = CatBrushHandler.handle(player, (Cat) (Object) this, player.level());
        if (consumed) {
            cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide()));
        }
    }
}
