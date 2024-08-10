package net.myworldzycpc.command_macro_key.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.LocalPlayer;

public class RepeatingMacro extends Macro{
    public static final Codec<RepeatingMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primaryKey").forGetter(Macro::getPrimaryKey),
            Codec.INT.fieldOf("modifierKey").forGetter(Macro::getModifierKey),
            Codec.STRING.fieldOf("command").forGetter(Macro::getCommand),
            Codec.BOOL.optionalFieldOf("hasOp",false).forGetter(Macro::hasOp),
            Codec.STRING.optionalFieldOf("located","unknown").forGetter(Macro::getLocated),
            Codec.INT.fieldOf("repeat").forGetter(RepeatingMacro::getRepeat),
            Codec.INT.fieldOf("interval").forGetter(RepeatingMacro::getInterval)
    ).apply(instance, RepeatingMacro::new));

    private final int repeat;
    private final int interval;
    private int remainingRepeat;

    public RepeatingMacro(int primaryKey, int modifierKey, String command, boolean hasOp,String located, int repeat, int interval) {
        super(primaryKey, modifierKey, command, hasOp,located);
        this.repeat = repeat;
        this.interval = interval;
    }

    public RepeatingMacro() {
        super();
        this.repeat = 0;
        this.interval = 0;
    }

    public int getRepeat() {
        return repeat;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public Codec<? extends Macro> codec() {
        return CODEC;
    }

    @Override
    public Macro type() {
        return MacroRegistry.REPEATING_MACRO.get();
    }
    @Override
    public void tick(LocalPlayer localPlayer, long gameTime) {
        if (timePressed == -1) {
            return;
        }
        if (remainingRepeat > 0) {
            if (gameTime - timePressed > interval) {
                timePressed = gameTime;
                execute(localPlayer);
                remainingRepeat--;
            }else {
                this.isRemove = false;
            }
        } else {
            timePressed = -1;
            this.isRemove = true;
            this.remainingRepeat = repeat;
        }
    }

    @Override
    public void execute(LocalPlayer player) {
        super.execute(player);
        this.isRemove = false;
    }
}
