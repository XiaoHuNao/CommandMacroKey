package com.xiaohunao.command_macro_key.event;

import com.xiaohunao.command_macro_key.CommandMacroKey;
import com.xiaohunao.command_macro_key.MacroManager;
import com.xiaohunao.command_macro_key.type.Macro;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || minecraft.player == null || event.getAction() != GLFW.GLFW_PRESS) {
            return;
        }

        int key = event.getKey();
        int modifiers = event.getModifiers();

        if (minecraft.level != null) {
            MacroManager macroManager = CommandMacroKey.MACRO_MANAGER;
            Stream.of(macroManager.getClientMacros())
                    .flatMap(Collection::stream)
                    .forEach(macro -> {
                        if (macro.isKeyTriggered(key, modifiers)) {
                            macro.setPressed(minecraft.level.getGameTime(), macro.hasOp());
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void onMacroManager(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (event.phase != TickEvent.Phase.END || level == null) return;
        long gameTime = level.getGameTime();

        Iterator<Map.Entry<UUID, Macro>> iterator = CommandMacroKey.MACRO_MANAGER.getMacros().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Macro> entry = iterator.next();
            Macro macro = entry.getValue();
            macro.tick(minecraft.player, gameTime);
            if (macro.isRemove()) {
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.isDeadOrDying() && event.player instanceof LocalPlayer) {
            CommandMacroKey.MACRO_MANAGER.getMacros().clear();
        }
    }

    @SubscribeEvent
    public static void onClientChat(ClientChatEvent event) {
        if (event.getMessage().equals("#macro reload")) {
            CommandMacroKey.reloadMacros();
            event.setCanceled(true);
        }
    }

}

