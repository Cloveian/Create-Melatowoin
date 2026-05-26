package net.melatowoin.event;

/**
 * Registers cross-platform event handlers using Architectury's event system.
 * Called from MelatowoinMod.init().
 *
 * Cat brushing is handled via MixinCatInteract (Cat.mobInteract) so it also
 * works with Create Deployers, which call entity.interact() directly without
 * firing the player interaction event.
 */
public class CommonEventHandlers {

    public static void register() {
    }
}
