package net.myworldzycpc.command_macro_key.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.LocalPlayer;

public class DelayedMacro extends Macro{
    public static final Codec<DelayedMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primaryKey").forGetter(Macro::getPrimaryKey),
            Codec.INT.fieldOf("modifierKey").forGetter(Macro::getModifierKey),
            Codec.STRING.fieldOf("command").forGetter(Macro::getCommand),
            Codec.BOOL.optionalFieldOf("hasOp",false).forGetter(Macro::hasOp),
            Codec.STRING.optionalFieldOf("located","unknown").forGetter(Macro::getLocated),
            Codec.INT.fieldOf("delay").forGetter(DelayedMacro::getDelay)
    ).apply(instance, DelayedMacro::new));

    private final int delay;

    public DelayedMacro(int primaryKey, int modifierKey, String command, boolean hasOp,String located, int delay) {
        super(primaryKey, modifierKey, command, hasOp,located);
        this.delay = delay;
    }

    public DelayedMacro() {
        super();
        this.delay = 0;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public Codec<? extends Macro> codec() {
        return CODEC;
    }

    @Override
    public Macro type() {
        return MacroRegistry.DELAYED_MACRO.get();
    }
    @Override
    public void tick(LocalPlayer localPlayer, long gameTime) {
        if (timePressed == -1) {
            return;
        }
        if (gameTime - timePressed > delay) {
            timePressed = -1;
            execute(localPlayer);
        }
    }
}
