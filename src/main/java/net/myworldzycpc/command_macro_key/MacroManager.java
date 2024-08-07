package net.myworldzycpc.command_macro_key;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myworldzycpc.command_macro_key.network.Messages;
import net.myworldzycpc.command_macro_key.network.message.MacroReloadPacket;
import net.myworldzycpc.command_macro_key.network.message.SyncMacrosPacket;
import net.myworldzycpc.command_macro_key.type.Macro;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MacroManager {
    private final ModConfig clientConfig = new ModConfig("macros-client");
    private final ModConfig serverConfig = new ModConfig("macros-server");
    private final Map<UUID, Macro> MACROS = Maps.newHashMap();
    private final List<Macro> clientMacros = Lists.newArrayList();
    private final List<Macro> serverMacros = Lists.newArrayList();

    private static final MacroManager INSTANCE = new MacroManager();

    public static MacroManager getInstance() {
        return INSTANCE;
    }

    private MacroManager() {
    }

    public void initClient() {
        clientConfig.init();
        clientMacros.addAll(clientConfig.getFileMacros());
    }

    public void initServer() {
        serverConfig.init();
    }

    public void macroReload() {
        clientMacros.clear();
        serverMacros.clear();
        initClient();
        initServer();
    }

    public void addMacro(UUID uuid, Macro macro) {
        MACROS.put(uuid, macro);
    }

    public void addMacro(Macro macro, boolean isClient) {
        if (isClient) {
            clientMacros.add(macro);
        } else {
            serverMacros.add(macro);
        }
    }

    public Map<UUID, Macro> getMacros() {
        return MACROS;
    }

    public List<Macro> getClientMacros() {
        return clientMacros;
    }

    public List<Macro> getServerMacros() {
        return serverMacros;
    }

    public ModConfig getClientConfig() {
        return clientConfig;
    }

    public ModConfig getServerConfig() {
        return serverConfig;
    }

    public static void reloadMacro(ServerPlayer player) {
        CommandMacroKey.MACRO_MANAGER.macroReload();
        Messages.NETWORK.send(new MacroReloadPacket(), PacketDistributor.PLAYER.with(player));
        CommandMacroKey.MACRO_MANAGER.getServerConfig().getFileMacros().stream()
                .filter(macroEntry -> !macroEntry.getCommand().isEmpty())
                .forEach(macroEntry -> {
                    Messages.NETWORK.send(new SyncMacrosPacket(macroEntry), PacketDistributor.PLAYER.with(player));
                });
    }
}