package com.xiaohunao.command_macro_key.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerMacrosPacket {
    private final String command;

    public ServerMacrosPacket(String command) {
        this.command = command;
    }
    public static void encode(ServerMacrosPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.command);
    }

    public static ServerMacrosPacket decode(FriendlyByteBuf buf) {
        return new ServerMacrosPacket(buf.readUtf());
    }

    public static void handle(ServerMacrosPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null) return;
            MinecraftServer server = serverPlayer.getServer();
            if (server == null) return;
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), msg.command);
        });
        context.setPacketHandled(true);
    }
}
