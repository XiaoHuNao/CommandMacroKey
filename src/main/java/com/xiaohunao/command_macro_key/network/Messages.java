package com.xiaohunao.command_macro_key.network;

import com.xiaohunao.command_macro_key.CommandMacroKey;
import com.xiaohunao.command_macro_key.network.message.MacroReloadPacket;
import com.xiaohunao.command_macro_key.network.message.ServerMacrosPacket;
import com.xiaohunao.command_macro_key.network.message.SyncMacrosPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static net.minecraftforge.network.NetworkRegistry.ACCEPTVANILLA;

public class Messages {
    private static final String PROTOCOL_VERSION = "0.0.1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CommandMacroKey.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            s -> s.equals(ACCEPTVANILLA) || PROTOCOL_VERSION.equals(s),
            s -> s.equals(ACCEPTVANILLA) || PROTOCOL_VERSION.equals(s)
    );

    public static void register() {
        NETWORK.registerMessage(0,
                SyncMacrosPacket.class,
                SyncMacrosPacket::encode,
                SyncMacrosPacket::decode,
                SyncMacrosPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK.registerMessage(1,
                ServerMacrosPacket.class,
                ServerMacrosPacket::encode,
                ServerMacrosPacket::decode,
                ServerMacrosPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK.registerMessage(2,
                MacroReloadPacket.class,
                MacroReloadPacket::encode,
                MacroReloadPacket::decode,
                MacroReloadPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));


    }
}