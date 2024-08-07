package net.myworldzycpc.command_macro_key;

import com.google.common.collect.Maps;
import net.myworldzycpc.command_macro_key.network.Messages;
import net.myworldzycpc.command_macro_key.type.MacroRegistry;
import net.minecraft.world.entity.player.Player;
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

import java.util.Map;
import java.util.function.Function;

@Mod(CommandMacroKey.MOD_ID)
public class CommandMacroKey {
    public static final String MOD_ID = "command_macro_key";
    public static final MacroManager MACRO_MANAGER = MacroManager.getInstance();
    public static final Map<String, Function<Player,String>> placeholderMap = Maps.newHashMap();
    public static final Logger LOGGER = LogManager.getLogger(CommandMacroKey.class);

    public CommandMacroKey() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MacroRegistry.MACRO.register(modEventBus);
//        PlaceholderRegistry.PLACEHOLDER.register(modEventBus);
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
        registerPlaceholder();
    }
    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        MacroReloadCommand.register(event.getDispatcher());
    }
    private void registerPlaceholder(){
        placeholderMap.put("player_name", player -> player.getGameProfile().getName());
        placeholderMap.put("player_pos_x", player -> String.valueOf(player.getX()));
        placeholderMap.put("player_pos_y", player -> String.valueOf(player.getY()));
        placeholderMap.put("player_pos_z", player -> String.valueOf(player.getZ()));
        placeholderMap.put("player_pos", player -> String.format("%s %s %s", player.getX(), player.getY(), player.getZ()));
        placeholderMap.put("player_health", player -> String.valueOf(player.getHealth()));
        placeholderMap.put("player_food", player -> String.valueOf(player.getFoodData().getFoodLevel()));
        placeholderMap.put("player_exp", player -> String.valueOf(player.experienceProgress));
        placeholderMap.put("player_level", player -> String.valueOf(player.experienceLevel));
        placeholderMap.put("player_dimension", player -> player.level().dimension().location().toString());
        placeholderMap.put("player_biome", player -> player.level().getBiome(player.blockPosition()).get().toString());
        placeholderMap.put("player_facing", player -> player.getDirection().toString());
        placeholderMap.put("player_x_rot", player -> String.valueOf(player.getXRot()));
        placeholderMap.put("player_y_rot", player -> String.valueOf(player.getYRot()));
        placeholderMap.put("player_uuid", player -> player.getGameProfile().getId().toString());
    }
}
