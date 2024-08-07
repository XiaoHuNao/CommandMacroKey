package net.myworldzycpc.command_macro_key.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SingleMacro extends Macro {
    public static final Codec<SingleMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primaryKey").forGetter(Macro::getPrimaryKey),
            Codec.INT.fieldOf("modifierKey").forGetter(Macro::getModifierKey),
            Codec.STRING.fieldOf("command").forGetter(Macro::getCommand),
            Codec.BOOL.optionalFieldOf("hasOp",false).forGetter(Macro::hasOp),
            Codec.STRING.optionalFieldOf("located","unknown").forGetter(Macro::getLocated)
    ).apply(instance, SingleMacro::new));


    public SingleMacro(int primaryKey, int modifierKey, String command,Boolean hasOp,String located) {
        super(primaryKey, modifierKey, command, hasOp,located);
    }

    public SingleMacro() {
        super();
    }


    @Override
    public Codec<? extends Macro> codec() {
        return CODEC;
    }

    @Override
    public Macro type() {
        return MacroRegistry.SINGLE_MACRO.get();
    }
}
