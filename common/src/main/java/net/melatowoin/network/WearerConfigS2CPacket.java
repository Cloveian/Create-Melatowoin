package net.melatowoin.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.server.WearerRenderConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

/**
 * Server → Client packet that pushes one player's render preferences out to
 * every other client, so each client renders that player according to the
 * player's own choices rather than the viewer's. Client receiver lives in
 * the platform initialisers.
 */
public class WearerConfigS2CPacket {

    public static final ResourceLocation ID =
            new ResourceLocation(MelatowoinMod.MOD_ID, "wearer_config_s2c");

    public final UUID playerId;
    public final WearerRenderConfig config;

    public WearerConfigS2CPacket(UUID playerId, WearerRenderConfig config) {
        this.playerId = playerId;
        this.config = config;
    }

    public WearerConfigS2CPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        byte b = buf.readByte();
        this.config = new WearerRenderConfig(
                (b & 1) != 0,
                (b & 2) != 0,
                (b & 4) != 0);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        int b = (config.liftEars()       ? 1 : 0)
              | (config.hideHelmet()     ? 2 : 0)
              | (config.hideChestplate() ? 4 : 0);
        buf.writeByte(b);
    }

    public static void sendToPlayer(ServerPlayer player, WearerConfigS2CPacket pkt) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        pkt.encode(buf);
        NetworkManager.sendToPlayer(player, ID, buf);
    }

    /** Called once during common init so the ID class is loaded. */
    public static void register() {
        // intentionally empty; client-side receiver lives in the platform initialisers.
    }
}
