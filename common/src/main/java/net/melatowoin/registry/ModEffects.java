package net.melatowoin.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.effect.ChangeEffect;
import net.melatowoin.effect.EepyEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.MOB_EFFECT);

    public static final RegistrySupplier<MobEffect> EEPY =
            EFFECTS.register("eepy", EepyEffect::new);

    public static final RegistrySupplier<MobEffect> CHANGE =
            EFFECTS.register("change", ChangeEffect::new);

    public static void register() {
        EFFECTS.register();
    }
}
