package net.melatowoin.registry;

import net.minecraft.world.level.GameRules;

public class ModGameRules {

    /**
     * Controls whether brushing a cat drops fur.
     * Default: true
     */
    public static final GameRules.Key<GameRules.BooleanValue> DO_CAT =
            GameRules.register("docat", GameRules.Category.PLAYER,
                    GameRules.BooleanValue.create(true));

    public static void register() {
        // Game rules are registered via the static field initialiser above.
        // This method exists so MelatowoinMod.init() can call it explicitly
        // to ensure the class is loaded early.
    }
}
