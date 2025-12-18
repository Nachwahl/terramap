package fr.thesmyler.terramap.network;

import fr.thesmyler.terramap.TerramapClientContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

/**
 * Server to Client packet containing teleportation command
 * Converted from Forge IMessage to Fabric packet
 */
public class S2CTpCommandPacket {

    private final String cmd;

    public S2CTpCommandPacket(String cmd) {
        this.cmd = cmd;
    }

    /**
     * Encode packet data to buffer
     */
    public void encode(PacketByteBuf buf) {
        NetworkUtil.encodeStringToByteBuf(this.cmd, buf);
    }

    /**
     * Decode packet data from buffer
     */
    public static S2CTpCommandPacket decode(PacketByteBuf buf) {
        String cmd = NetworkUtil.decodeStringFromByteBuf(buf);
        return new S2CTpCommandPacket(cmd);
    }

    /**
     * Handle the packet on the client side
     */
    @Environment(EnvType.CLIENT)
    public void handle() {
        MinecraftClient.getInstance().execute(() -> {
            TerramapClientContext.getContext().setTpCommand(this.cmd);
        });
    }

    public String getCommand() {
        return this.cmd;
    }
}
