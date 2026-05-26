package net.melatowoin.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.entity.CyanProjectileEntity;
import net.melatowoin.entity.OrangeArrowEntity;
import net.melatowoin.entity.OrangeProjectileEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(MelatowoinMod.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<CyanProjectileEntity>> CYAN_PROJECTILE =
            ENTITY_TYPES.register("cyan_projectile", () ->
                    EntityType.Builder.<CyanProjectileEntity>of(CyanProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("cyan_projectile"));

    public static final RegistrySupplier<EntityType<OrangeProjectileEntity>> ORANGE_PROJECTILE =
            ENTITY_TYPES.register("orange_projectile", () ->
                    EntityType.Builder.<OrangeProjectileEntity>of(OrangeProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("orange_projectile"));

    public static final RegistrySupplier<EntityType<OrangeArrowEntity>> ORANGE_ARROW =
            ENTITY_TYPES.register("orange_arrow", () ->
                    EntityType.Builder.<OrangeArrowEntity>of(OrangeArrowEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("orange_arrow"));

    public static void register() {
        ENTITY_TYPES.register();
    }
}
