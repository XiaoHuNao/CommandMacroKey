package net.myworldzycpc.command_macro_key.network.message;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

    public static void handle(ServerMacrosPacket msg, CustomPayloadEvent.Context ctx) {
        CustomPayloadEvent.Context context = ctx;
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null) return;
            CommandSourceStack commandSourceStack = new CommandSourceStack(serverPlayer, serverPlayer.position(), serverPlayer.getRotationVector(),
                    serverPlayer.level() instanceof ServerLevel ? (ServerLevel) serverPlayer.level() : null,
                    4, serverPlayer.getName().getString(), serverPlayer.getDisplayName(), serverPlayer.level().getServer(), serverPlayer);
            serverPlayer.getServer().getCommands().performPrefixedCommand(commandSourceStack, msg.command);
        });
        context.setPacketHandled(true);
    }
}
