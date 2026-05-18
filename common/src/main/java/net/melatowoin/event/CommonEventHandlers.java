package net.melatowoin.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.world.entity.player.Player;

/**
 * Registers cross-platform event handlers using Architectury's event system.
 * Called from MelatowoinMod.init().
 */
public class CommonEventHandlers {

    public static void register() {
        // Cat brushing: right-click entity
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            boolean consumed = CatBrushHandler.handle(player, entity, player.level());
            return consumed ? EventResult.interruptTrue() : EventResult.pass();
        });
    }
}
