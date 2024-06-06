package com.xiaohunao.command_macro_key;

import com.xiaohunao.command_macro_key.network.Messages;
import com.xiaohunao.command_macro_key.type.MacroRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CommandMacroKey.MOD_ID)
public class CommandMacroKey {
    public static final String MOD_ID = "command_macro_key";
    public static final MacroManager MACRO_MANAGER = MacroManager.getInstance();
    public static final Logger LOGGER = LogManager.getLogger(CommandMacroKey.class);

    public CommandMacroKey() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MacroRegistry.MACRO.register(modEventBus);
        modEventBus.addListener(this::setupServer);
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::setupCommon);
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setupClient(FMLClientSetupEvent event) {
        MACRO_MANAGER.initClient();
    }

    private void setupServer(FMLDedicatedServerSetupEvent event) {
        MACRO_MANAGER.initServer();
    }
    @SubscribeEvent
    public void setupCommon(FMLCommonSetupEvent event) {
        Messages.register();
    }
    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        MacroReloadCommand.register(event.getDispatcher());
    }
}
