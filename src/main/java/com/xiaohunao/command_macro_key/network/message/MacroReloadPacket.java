package com.xiaohunao.command_macro_key.network.message;

import com.xiaohunao.command_macro_key.CommandMacroKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MacroReloadPacket {
    public MacroReloadPacket() {
    }
    public static MacroReloadPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new MacroReloadPacket();
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }
    public static void handle(MacroReloadPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CommandMacroKey.MACRO_MANAGER::macroReload);
        });
        ctx.get().setPacketHandled(true);
    }
}
