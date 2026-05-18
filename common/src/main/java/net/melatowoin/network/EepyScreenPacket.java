package net.melatowoin.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.melatowoin.MelatowoinMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Server → Client packet that tells the client to open or close the Eepy (sleeping) screen.
 * Registration of the S2C receiver is done in the platform-specific client entry point.
 */
public class EepyScreenPacket {

    public static final ResourceLocation ID =
            new ResourceLocation(MelatowoinMod.MOD_ID, "eepy_screen");

    /** True = open the screen, false = close it. */
    public final boolean open;

    public EepyScreenPacket(boolean open) {
        this.open = open;
    }

    public EepyScreenPacket(FriendlyByteBuf buf) {
        this.open = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.open);
    }

    // ---- Server-side send helpers ----

    public static void sendOpen(Player player) {
        send(player, true);
    }

    public static void sendClose(Player player) {
        send(player, false);
    }

    private static void send(Player player, boolean open) {
        if (player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            new EepyScreenPacket(open).encode(buf);
            NetworkManager.sendToPlayer(serverPlayer, ID, buf);
        }
    }

    /**
     * Called once during mod init to ensure the packet ID class is loaded.
     * Receiver registration happens in the platform client entry point.
     */
    public static void register() {
        // intentionally empty — receiver is registered per-platform in the client initializer
    }
}
