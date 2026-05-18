package net.melatowoin.fabric.event;

/**
 * Fabric-side event handlers.
 *
 * Cat-brushing is handled via Architectury's cross-platform InteractionEvent in
 * {@link net.melatowoin.event.CommonEventHandlers}.
 *
 * Mob-effect lifecycle hooks are handled via
 * {@link net.melatowoin.fabric.mixin.MixinLivingEntityEffects}, because
 * Fabric API 0.91.x does not expose mob-effect lifecycle events.
 */
public class FabricEventHandlers {

    public static void register() {
        // No additional Fabric-specific event registrations needed currently.
    }
}
