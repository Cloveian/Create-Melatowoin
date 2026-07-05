package net.melatowoin.advancements;

import net.melatowoin.MelatowoinMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom criterion triggers registered with vanilla's {@link CriteriaTriggers}.
 * Initialised once during common mod init by referencing the static fields.
 */
public class ModCriteria {

    public static final SimplePlayerTrigger SILENT_SCULK_PASS         = register("silent_sculk_pass");
    public static final SimplePlayerTrigger SILENT_CHEST_OPEN         = register("silent_chest_open");
    public static final SimplePlayerTrigger APPLIED_CHANGE_TO_OTHER   = register("applied_change_to_other");
    public static final SimplePlayerTrigger HIT_BY_ORANGE             = register("hit_by_orange");

    private static SimplePlayerTrigger register(String name) {
        return CriteriaTriggers.register(new SimplePlayerTrigger(
                new ResourceLocation(MelatowoinMod.MOD_ID, name)));
    }

    /** Force-loads this class so the static initialisers run during mod init. */
    public static void init() {
        // intentionally empty
    }
}
