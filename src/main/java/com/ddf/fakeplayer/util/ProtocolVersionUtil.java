package com.ddf.fakeplayer.util;

import com.ddf.fakeplayer.Resources;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.nbt.util.stream.LittleEndianDataInputStream;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v465.Bedrock_v465;
import org.cloudburstmc.protocol.bedrock.codec.v567.Bedrock_v567;
import org.cloudburstmc.protocol.bedrock.codec.v568.Bedrock_v568;
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575;
import org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582;
import org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589;
import org.cloudburstmc.protocol.bedrock.codec.v594.Bedrock_v594;
import org.cloudburstmc.protocol.bedrock.codec.v618.Bedrock_v618;
import org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630;
import org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649;
import org.cloudburstmc.protocol.bedrock.codec.v662.Bedrock_v662;
import org.cloudburstmc.protocol.bedrock.codec.v671.Bedrock_v671;
import org.cloudburstmc.protocol.bedrock.codec.v685.Bedrock_v685;
import org.cloudburstmc.protocol.bedrock.codec.v686.Bedrock_v686;
import org.cloudburstmc.protocol.bedrock.codec.v712.Bedrock_v712;
import org.cloudburstmc.protocol.bedrock.codec.v729.Bedrock_v729;
import org.cloudburstmc.protocol.bedrock.codec.v748.Bedrock_v748;
import org.cloudburstmc.protocol.bedrock.codec.v766.Bedrock_v766;
import org.cloudburstmc.protocol.bedrock.codec.v776.Bedrock_v776;
import org.cloudburstmc.protocol.bedrock.codec.v786.Bedrock_v786;
import org.cloudburstmc.protocol.bedrock.codec.v800.Bedrock_v800;
import org.cloudburstmc.protocol.bedrock.codec.v818.Bedrock_v818;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtocolVersionUtil {
    private static Map<Integer, BedrockCodec> codecMap = new HashMap<>();
    private static Map<BedrockCodec, Integer> rakNetVersionMap = new HashMap<>();
    private static Map<BedrockCodec, List<NbtMap>> blockPaletteMap = new HashMap<>();
    private static BedrockCodec latestBedrockCodec;

    static {
        registerPacketCodec(Bedrock_v465.CODEC, 11, null);
        registerPacketCodec(Bedrock_v567.CODEC, 11, null);
        registerPacketCodec(Bedrock_v568.CODEC, 11, null);
        registerPacketCodec(Bedrock_v568.CODEC, 11, null);
        registerPacketCodec(Bedrock_v575.CODEC, 11, null);
        registerPacketCodec(Bedrock_v582.CODEC, 11, null);
        registerPacketCodec(Bedrock_v589.CODEC, 11, null);
        registerPacketCodec(Bedrock_v594.CODEC, 11, null);
        registerPacketCodec(Bedrock_v618.CODEC, 11, null);
        registerPacketCodec(Bedrock_v630.CODEC, 11, null);
        registerPacketCodec(Bedrock_v649.CODEC, 11, null);
        registerPacketCodec(Bedrock_v662.CODEC, 11, null);
        registerPacketCodec(Bedrock_v671.CODEC, 11, null);
        registerPacketCodec(Bedrock_v685.CODEC, 11, null);
        registerPacketCodec(Bedrock_v686.CODEC, 11, null);
        registerPacketCodec(Bedrock_v712.CODEC, 11, null);
        registerPacketCodec(Bedrock_v729.CODEC, 11, null);
        registerPacketCodec(Bedrock_v748.CODEC, 11, null);
        registerPacketCodec(Bedrock_v766.CODEC, 11, null);
        registerPacketCodec(Bedrock_v776.CODEC, 11, null);
        registerPacketCodec(Bedrock_v786.CODEC, 11, null);
        registerPacketCodec(Bedrock_v800.CODEC, 11, null);
        registerPacketCodec(Bedrock_v818.CODEC, 11, null);

        codecMap = Collections.unmodifiableMap(codecMap);
        rakNetVersionMap = Collections.unmodifiableMap(rakNetVersionMap);
        blockPaletteMap = Collections.unmodifiableMap(blockPaletteMap);
    }

    private static void registerPacketCodec(BedrockCodec codec, int rakNetProtocolVersion, String blockPalette) {
        codecMap.put(codec.getProtocolVersion(), codec);
        rakNetVersionMap.put(codec, rakNetProtocolVersion);
        if (blockPalette != null) {
            try (NBTInputStream nbtInputStream = new NBTInputStream(new LittleEndianDataInputStream(Resources.getResAsStream("/blockpalette/" + blockPalette)))) {
                Object tag = nbtInputStream.readTag();
                if (tag instanceof NbtMap) {
                    blockPaletteMap.put(codec, ((NbtMap) tag).getList("blocks", NbtType.COMPOUND));
                }
            } catch (IOException ignored) {
            }
        }
        latestBedrockCodec = codec;
    }

    public static BedrockCodec getPacketCodec(int protocolVersion) {
        BedrockCodec codec = codecMap.get(protocolVersion);
        if (codec == null) {
            throw new IllegalArgumentException("Unsupported protocol version: " + protocolVersion);
        }
        return codec;
    }

    public static List<NbtMap> getBlockPalette(BedrockCodec codec) {
        return blockPaletteMap.get(codec);
    }

    public static int getRakNetProtocolVersion(BedrockCodec codec) {
        return rakNetVersionMap.get(codec);
    }

    public static BedrockCodec getLatestBedrockCodec() {
        return latestBedrockCodec;
    }
}
