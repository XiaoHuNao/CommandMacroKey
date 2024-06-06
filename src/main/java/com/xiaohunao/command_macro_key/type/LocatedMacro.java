package com.xiaohunao.command_macro_key.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.storage.LevelResource;

import java.util.Optional;

public class LocatedMacro extends Macro{
    public static final Codec<LocatedMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primaryKey").forGetter(Macro::getPrimaryKey),
            Codec.INT.fieldOf("modifierKey").forGetter(Macro::getModifierKey),
            Codec.STRING.fieldOf("command").forGetter(Macro::getCommand),
            Codec.BOOL.optionalFieldOf("hasOp",false).forGetter(Macro::hasOp),
            Codec.STRING.fieldOf("location").forGetter(LocatedMacro::getLocation)
    ).apply(instance, LocatedMacro::new));

    private final String location;

    public LocatedMacro(int primaryKey, int modifierKey, String command, boolean hasOp, String location) {
        super(primaryKey, modifierKey, command, hasOp);
        this.location = location;
    }

    public LocatedMacro() {
        super();
        this.location = "";
    }

    public String getLocation() {
        return location;
    }

    @Override
    public Codec<? extends Macro> codec() {
        return CODEC;
    }

    @Override
    public Macro type() {
        return MacroRegistry.LOCATED_MACRO.get();
    }

    @Override
    public void execute(LocalPlayer player) {
        if (this.location.equals(LocatedMacro.getCurrentLocation())) {
            super.execute(player);
        }
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
}
