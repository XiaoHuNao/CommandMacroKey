package net.myworldzycpc.command_macro_key;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.myworldzycpc.command_macro_key.type.Macro;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MacroManager {
    private final ModConfig clientConfig = new ModConfig("macros-client");
    private final Map<UUID, Macro> MACROS = Maps.newHashMap();
    private final List<Macro> clientMacros = Lists.newArrayList();
    private static final Minecraft minecraft = Minecraft.getInstance();

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

    public void macroReload() {
        clientMacros.clear();
        initClient();
    }

    public void addMacro(UUID uuid, Macro macro) {
        MACROS.put(uuid, macro);
    }

    public void addMacro(Macro macro, boolean isClient) {
        if (isClient) {
            clientMacros.add(macro);
        }
    }

    public Map<UUID, Macro> getMacros() {
        return MACROS;
    }

    public List<Macro> getClientMacros() {
        return clientMacros;
    }

    public ModConfig getClientConfig() {
        return clientConfig;
    }

    public static void reloadMacro() {
        CommandMacroKey.MACRO_MANAGER.macroReload();
    }
}