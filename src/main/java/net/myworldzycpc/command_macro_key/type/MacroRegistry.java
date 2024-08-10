package net.myworldzycpc.command_macro_key.type;

import net.myworldzycpc.command_macro_key.CommandMacroKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MacroRegistry {
    public static final ResourceKey<Registry<Macro>> KEY = ResourceKey.createRegistryKey(new ResourceLocation(CommandMacroKey.MOD_ID,"macro"));
    public static final DeferredRegister<Macro> MACRO = DeferredRegister.create(KEY, CommandMacroKey.MOD_ID);
    public static final Supplier<IForgeRegistry<Macro>> REGISTRY = MACRO.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<Macro> SINGLE_MACRO = MACRO.register("single", SingleMacro::new);
    public static final RegistryObject<Macro> DELAYED_MACRO = MACRO.register("delayed", DelayedMacro::new);
    public static final RegistryObject<Macro> REPEATING_MACRO = MACRO.register("repeating", RepeatingMacro::new);


}
