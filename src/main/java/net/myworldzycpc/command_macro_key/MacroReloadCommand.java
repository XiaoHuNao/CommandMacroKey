package net.myworldzycpc.command_macro_key;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MacroReloadCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("macro")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                .then(Commands.literal("reload").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    context.getSource().sendSuccess(() -> Component.literal("Macros reloaded"), true);
                    MacroManager.reloadMacro(player);
                    return 1;
                })));
    }
}
