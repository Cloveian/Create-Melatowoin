package net.melatowoin.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.melatowoin.MelatowoinMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Server → Client packet that tags a position-only sound the server is about
 * to broadcast with the entity id of the player that caused it, plus a
 * snapshot of which cat pieces that player currently has equipped (computed
 * server-side where the equipment state is authoritative).
 *
 * The client uses the bitmask directly to decide whether to muffle the sound
 * — bypassing its own equipment lookup, which can lag by tens of seconds
 * while the Accessories mod finishes syncing capability data after world load.
 *
 * Bitmask layout:
 *   bit 0 — cat_ears equipped
 *   bit 1 — tail equipped
 *   bit 2 — paws equipped
 *   bit 3 — toe_beans equipped
 */
public class SoundSourceHintPacket {

    public static final ResourceLocation ID =
            new ResourceLocation(MelatowoinMod.MOD_ID, "sound_source_hint");

    public static final int FLAG_CAT_EARS  = 1;
    public static final int FLAG_TAIL      = 1 << 1;
    public static final int FLAG_PAWS      = 1 << 2;
    public static final int FLAG_TOE_BEANS = 1 << 3;

    public final int sourceEntityId;
    public final double x;
    public final double y;
    public final double z;
    public final ResourceLocation soundId;
    public final byte equipmentFlags;

    public SoundSourceHintPacket(int sourceEntityId, double x, double y, double z,
                                 ResourceLocation soundId, byte equipmentFlags) {
        this.sourceEntityId = sourceEntityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.soundId = soundId;
        this.equipmentFlags = equipmentFlags;
    }

    public SoundSourceHintPacket(FriendlyByteBuf buf) {
        this.sourceEntityId = buf.readVarInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.soundId = buf.readResourceLocation();
        this.equipmentFlags = buf.readByte();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(sourceEntityId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeResourceLocation(soundId);
        buf.writeByte(equipmentFlags);
    }

    public boolean hasFlag(int flag) {
        return (equipmentFlags & flag) != 0;
    }

    public static void sendToPlayer(ServerPlayer player, SoundSourceHintPacket pkt) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        pkt.encode(buf);
        NetworkManager.sendToPlayer(player, ID, buf);
    }

    public static void register() {
        // intentionally empty; receiver is registered in the platform client initialisers.
    }
}
