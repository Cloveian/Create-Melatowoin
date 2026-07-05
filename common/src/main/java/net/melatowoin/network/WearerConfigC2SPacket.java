package net.melatowoin.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.melatowoin.MelatowoinMod;
import net.melatowoin.server.WearerConfigStore;
import net.melatowoin.server.WearerRenderConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Client → Server packet sent whenever the local player saves their render
 * config. The server records the new preferences in {@link WearerConfigStore}
 * and re-broadcasts them to everyone (including the sender, so multi-tab
 * setups stay in sync).
 */
public class WearerConfigC2SPacket {

    public static final ResourceLocation ID =
            new ResourceLocation(MelatowoinMod.MOD_ID, "wearer_config_c2s");

    public final boolean liftEars;
    public final boolean hideHelmet;
    public final boolean hideChestplate;

    public WearerConfigC2SPacket(boolean liftEars, boolean hideHelmet, boolean hideChestplate) {
        this.liftEars = liftEars;
        this.hideHelmet = hideHelmet;
        this.hideChestplate = hideChestplate;
    }

    public WearerConfigC2SPacket(FriendlyByteBuf buf) {
        byte b = buf.readByte();
        this.liftEars       = (b & 1) != 0;
        this.hideHelmet     = (b & 2) != 0;
        this.hideChestplate = (b & 4) != 0;
    }

    public void encode(FriendlyByteBuf buf) {
        int b = (liftEars ? 1 : 0) | (hideHelmet ? 2 : 0) | (hideChestplate ? 4 : 0);
        buf.writeByte(b);
    }

    public static void sendToServer(boolean liftEars, boolean hideHelmet, boolean hideChestplate) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        new WearerConfigC2SPacket(liftEars, hideHelmet, hideChestplate).encode(buf);
        NetworkManager.sendToServer(ID, buf);
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, context) -> {
            WearerConfigC2SPacket pkt = new WearerConfigC2SPacket(buf);
            context.queue(() -> {
                ServerPlayer sender = (ServerPlayer) context.getPlayer();
                if (sender == null) return;
                WearerRenderConfig config = new WearerRenderConfig(
                        pkt.liftEars, pkt.hideHelmet, pkt.hideChestplate);
                WearerConfigStore.put(sender.getUUID(), config);

                // Broadcast the update to every connected player.
                MinecraftServer server = sender.getServer();
                if (server == null) return;
                for (ServerPlayer recipient : server.getPlayerList().getPlayers()) {
                    WearerConfigS2CPacket.sendToPlayer(recipient,
                            new WearerConfigS2CPacket(sender.getUUID(), config));
                }
            });
        });
    }
}
