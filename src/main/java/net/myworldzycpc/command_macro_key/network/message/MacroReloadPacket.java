package net.myworldzycpc.command_macro_key.network.message;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.myworldzycpc.command_macro_key.CommandMacroKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class MacroReloadPacket {
    public MacroReloadPacket() {
    }
    public static MacroReloadPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new MacroReloadPacket();
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }
    public static void handle(MacroReloadPacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CommandMacroKey.MACRO_MANAGER::macroReload);
        });
        ctx.setPacketHandled(true);
    }
}
