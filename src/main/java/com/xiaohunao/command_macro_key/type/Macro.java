package com.xiaohunao.command_macro_key.type;

import com.mojang.serialization.Codec;
import com.xiaohunao.command_macro_key.CommandMacroKey;
import com.xiaohunao.command_macro_key.network.Messages;
import com.xiaohunao.command_macro_key.network.message.ServerMacrosPacket;
import net.minecraft.client.player.LocalPlayer;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class Macro {
    public static final Supplier<Codec<Macro>> CODEC = () -> MacroRegistry.REGISTRY.get().getCodec()
            .dispatch("macro", Macro::type, Macro::codec);


    protected final UUID id = UUID.randomUUID();
    protected final int primaryKey;
    protected final int modifierKey;
    protected final String command;
    protected long timePressed = -1;
    protected Boolean hasOp;
    protected boolean isRemove;

    public Macro(int primaryKey, int modifierKey, String command,Boolean hasOp) {
        this.primaryKey = primaryKey;
        this.modifierKey = modifierKey;
        this.command = command;
        this.hasOp = hasOp;
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
        String[] messages = command.split("(?<!\\\\)(?:\\\\\\\\)*\\\\n");
        for (String message : messages) {
            String processedMessage = message.replace("\\\\n", "\\n");
            if (processedMessage.startsWith("/")) {
                if (hasOp) {
                    Messages.NETWORK.sendToServer(new ServerMacrosPacket(processedMessage));
                }else {
                    player.connection.sendCommand(processedMessage.substring(1));
                }
            } else {
                player.connection.sendChat(processedMessage);
            }
            this.isRemove = true;
        }
    }

    public boolean isSameMacro(Macro macro){
        return getPrimaryKey() == macro.getPrimaryKey() && getModifierKey() == macro.getModifierKey();
    }

    public abstract Codec<? extends Macro> codec();
    public abstract Macro type();
}