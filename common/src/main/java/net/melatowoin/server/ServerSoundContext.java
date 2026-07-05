package net.melatowoin.server;

import net.minecraft.server.level.ServerPlayer;

/**
 * Thread-local marker that records which {@link ServerPlayer}'s action is
 * currently being processed by the server. Set/cleared by mixins on the
 * relevant {@code ServerGamePacketListenerImpl} handlers (use item, use item
 * on block, interact, player action). Read by the {@code playSeededSound}
 * mixin so it can attribute the sound to that player and broadcast a hint
 * packet.
 *
 * Uses a single {@link ThreadLocal} because:
 *  - Server packet handlers run on the main server thread (after
 *    {@code PacketUtils.ensureRunningOnSameThread} succeeds).
 *  - Sounds emitted during the handler's call stack run on the same thread.
 *  - One leak on the netty thread (from an exception thrown by
 *    {@code ensureRunningOnSameThread}) only affects the netty thread's
 *    thread-local and never the main-thread reader.
 */
public final class ServerSoundContext {

    private static final ThreadLocal<ServerPlayer> CURRENT = new ThreadLocal<>();

    private ServerSoundContext() {}

    public static void enter(ServerPlayer player) {
        CURRENT.set(player);
    }

    public static void exit() {
        CURRENT.remove();
    }

    public static ServerPlayer get() {
        return CURRENT.get();
    }
}
