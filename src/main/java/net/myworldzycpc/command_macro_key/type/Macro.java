package net.myworldzycpc.command_macro_key.type;

import com.mojang.serialization.Codec;
import net.myworldzycpc.command_macro_key.CommandMacroKey;
import net.myworldzycpc.command_macro_key.network.Messages;
import net.myworldzycpc.command_macro_key.network.message.ServerMacrosPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Macro {
    public static final Supplier<Codec<Macro>> CODEC = () -> MacroRegistry.REGISTRY.get().getCodec()
            .dispatch("macro", Macro::type, Macro::codec);


    protected final UUID id = UUID.randomUUID();
    protected final int primaryKey;
    protected final int modifierKey;
    protected final String command;
    protected long timePressed = -1;
    protected Boolean hasOp;
    protected String located;
    protected boolean isRemove;

    public Macro(int primaryKey, int modifierKey, String command,Boolean hasOp, String located) {
        this.primaryKey = primaryKey;
        this.modifierKey = modifierKey;
        this.command = command;
        this.hasOp = hasOp;
        this.located = located;
    }

    public Macro() {
        this.primaryKey = -1;
        this.modifierKey = 0;
        this.command = "";
        this.hasOp = false;
    }

    public UUID getId() {
        return id;
    }

    public Boolean hasOp() {
        return this.hasOp;
    }
    public int getPrimaryKey() {
        return primaryKey;
    }
    public int getModifierKey() {
        return modifierKey;
    }
    public String getCommand() {
        return command;
    }

    public long getTimePressed() {
        return timePressed;
    }

    public boolean isRemove() {
        return isRemove;
    }
    public String getLocated() {
        return located;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public Boolean isKeyTriggered(int key, int modifiers) {
        int modifierKey = this.modifierKey;
        int primaryKey = this.primaryKey;
        return key == primaryKey && modifiers == modifierKey;
    }
    public void setPressed(long timePressed,boolean hasOp) {
        this.timePressed = timePressed;
        this.hasOp = hasOp;
        CommandMacroKey.MACRO_MANAGER.addMacro(this.getId(),this);
    }
    public void setHasOp(boolean hasOp) {
        this.hasOp = hasOp;
    }

    public void tick(LocalPlayer localPlayer, long gameTime){
        if (timePressed == -1) {
            return;
        }
        if (gameTime - timePressed > 0) {
            timePressed = -1;
            execute(localPlayer);
        }
    }
    public void execute(LocalPlayer player){
        if (!this.located.equals(getCurrentLocation()) && !this.located.equals("unknown")) {
            CommandMacroKey.LOGGER.error("Command macro address '{}' does not match location '{}'", this.located, getCurrentLocation());
            return;
        }

        String[] messages = command.split("\n");
        for (String message : messages) {
//            message = replacePlaceholder(message, player);
            Pattern pattern = Pattern.compile("\\%([^%]+)\\%");
            Matcher matcher = pattern.matcher(message);
            String finalCommand = message;
            while (matcher.find()) {
                String group = matcher.group();
                String placeholder = group.substring(1, group.length() - 1);
                Function<Player, String> playerStringFunction = CommandMacroKey.placeholderMap.get(placeholder);
                if (playerStringFunction != null) {
                    finalCommand = finalCommand.replace(group,playerStringFunction.apply(player));
                }
            }
            message = finalCommand;


            if (message.startsWith("/")) {
                if (hasOp) {
                    Messages.NETWORK.sendToServer(new ServerMacrosPacket(message));
                }else {
                    player.connection.sendCommand(message.substring(1));
                }
            } else {
                player.connection.sendChat(message);
            }
            this.isRemove = true;
        }
    }


    public boolean isSameMacro(Macro macro){
        return getPrimaryKey() == macro.getPrimaryKey() && getModifierKey() == macro.getModifierKey();
    }

    public static String getCurrentLocation() {
        Minecraft minecraftInstance = Minecraft.getInstance();
        if (minecraftInstance.getConnection() == null) {
            return "";
        }

        Optional<IntegratedServer> singlePlayerServer = Optional.ofNullable(minecraftInstance.getSingleplayerServer());
        if (singlePlayerServer.isPresent()) {
            return singlePlayerServer.get().getWorldPath(new LevelResource("")).toFile().getPath();
        } else {
            Optional<ServerData> currentServer = Optional.ofNullable(minecraftInstance.getCurrentServer());
            return currentServer.map(serverData -> serverData.ip).orElse("");
        }
    }
    public abstract Codec<? extends Macro> codec();
    public abstract Macro type();
}