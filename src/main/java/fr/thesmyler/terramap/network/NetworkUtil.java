package fr.thesmyler.terramap.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.PacketByteBuf;

/**
 * Utility class for encoding/decoding data in network packets
 * Updated for Fabric's PacketByteBuf (replaces Forge's ByteBuf/PacketBuffer)
 */
public final class NetworkUtil {
    
    private NetworkUtil() {}
    
    /**
     * Encode a string to a PacketByteBuf
     */
    public static void encodeStringToByteBuf(String str, PacketByteBuf buf) {
        buf.writeString(str);
    }

    /**
     * Decode a string from a PacketByteBuf
     */
    public static String decodeStringFromByteBuf(PacketByteBuf buf) {
        return buf.readString(Integer.MAX_VALUE/4);
    }

    /**
     * Encode a string array to a PacketByteBuf
     */
    public static void encodeStringArrayToByteBuf(String[] strings, PacketByteBuf buf) {
        buf.writeVarInt(strings.length);
        for (String str : strings) {
            buf.writeString(str);
        }
    }

    /**
     * Decode a string array from a PacketByteBuf
     */
    public static String[] decodeStringArrayFromByteBuf(PacketByteBuf buf) {
        int strCount = buf.readVarInt();
        String[] strings = new String[strCount]; 
        for (int i = 0; i < strCount; i++) {
            strings[i] = buf.readString(Integer.MAX_VALUE/4);
        }
        return strings;
    }
    
    /**
     * Encode a string map to a PacketByteBuf
     */
    public static void encodeStringMapToByteBuf(Map<String, String> map, PacketByteBuf buf) {
        buf.writeInt(map.size());
        for (String key : map.keySet()) {
            encodeStringToByteBuf(key, buf);
            encodeStringToByteBuf(map.get(key), buf);
        }
    }
    
    /**
     * Decode a string map from a PacketByteBuf
     */
    public static Map<String, String> decodeStringMapFromByteBuf(PacketByteBuf buf) {
        Map<String, String> map = new HashMap<>();
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            String key = decodeStringFromByteBuf(buf);
            String value = decodeStringFromByteBuf(buf);
            map.put(key, value);
        }
        return map;
    }

}
