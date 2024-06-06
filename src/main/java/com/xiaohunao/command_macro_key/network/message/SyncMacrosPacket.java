package com.xiaohunao.command_macro_key.network.message;

import com.xiaohunao.command_macro_key.CommandMacroKey;
import com.xiaohunao.command_macro_key.type.Macro;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMacrosPacket {
    private final Macro macro;
    public SyncMacrosPacket(Macro macro) {
        this.macro = macro;
    }
    public static void encode(SyncMacrosPacket packet, FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(Macro.CODEC.get(), packet.macro);
    }

    public static SyncMacrosPacket decode(FriendlyByteBuf buf) {
        return new SyncMacrosPacket(buf.readJsonWithCodec(Macro.CODEC.get()));
    }

    public Macro getMacro() {
        return macro;
    }

    public static void handle(SyncMacrosPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Macro macro = msg.getMacro();
                if (macro.hasOp()) {
                    CommandMacroKey.MACRO_MANAGER.addMacro(macro, false);
                } else {
                    CommandMacroKey.MACRO_MANAGER.addMacro(macro, true);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
