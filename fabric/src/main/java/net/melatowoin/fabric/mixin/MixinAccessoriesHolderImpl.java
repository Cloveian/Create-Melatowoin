package net.melatowoin.fabric.mixin;

import io.wispforest.accessories.api.AccessoriesHolder;
import io.wispforest.accessories.impl.AccessoriesHolderImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AccessoriesHolderImpl.class)
public class MixinAccessoriesHolderImpl {

    @Inject(method = "cosmeticsShown(Z)Lio/wispforest/accessories/api/AccessoriesHolder;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void forceVisible(boolean value, CallbackInfoReturnable<AccessoriesHolder> cir) {
        if (!value) {
            cir.setReturnValue((AccessoriesHolder) (Object) this);
        }
    }
}
