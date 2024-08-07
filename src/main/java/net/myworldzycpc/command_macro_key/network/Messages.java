package net.myworldzycpc.command_macro_key.network;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;
import net.myworldzycpc.command_macro_key.CommandMacroKey;
import net.myworldzycpc.command_macro_key.network.message.MacroReloadPacket;
import net.myworldzycpc.command_macro_key.network.message.ServerMacrosPacket;
import net.myworldzycpc.command_macro_key.network.message.SyncMacrosPacket;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.BiConsumer;


public class Messages {
    private static final String PROTOCOL_VERSION = "0.0.1";
    public static final SimpleChannel NETWORK = ChannelBuilder
            .named(new ResourceLocation(CommandMacroKey.MOD_ID, "main"))
            .networkProtocolVersion(1)
            .acceptedVersions(Channel.VersionTest.ACCEPT_VANILLA)
            .simpleChannel();

    public static void register() {
        NETWORK
                .messageBuilder(SyncMacrosPacket.class, 0)
                .encoder(SyncMacrosPacket::encode)
                .decoder(SyncMacrosPacket::decode)
                .consumerNetworkThread(SyncMacrosPacket::handle)
                .add();

        NETWORK
                .messageBuilder(ServerMacrosPacket.class, 1)
                .encoder(ServerMacrosPacket::encode)
                .decoder(ServerMacrosPacket::decode)
                .consumerNetworkThread(ServerMacrosPacket::handle)
                .add();

        NETWORK
                .messageBuilder(MacroReloadPacket.class, 2)
                .encoder(MacroReloadPacket::encode)
                .decoder(MacroReloadPacket::decode)
                .consumerNetworkThread(MacroReloadPacket::handle)
                .add();

    }
}