package net.myworldzycpc.command_macro_key;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.JsonOps;
import net.myworldzycpc.command_macro_key.type.Macro;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Macro.class, new MacroDeserializer())
            .setPrettyPrinting()
            .create();

    private final File file;
    private final List<Macro> fileMacros = Lists.newArrayList();
    public ModConfig(String fileName) {
        this.file = new File("config", fileName + ".json5");
    }

    public void init() {
        if (!file.exists()) {
            this.saveData();
        } else {
            this.load();
        }
    }

    private void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            this.fileMacros.clear();
            //判断是否是客户端配置文件
            Object fromJson = GSON.fromJson(json, getMacroListType());
            if (file.getName().contains("client")){
                if (fromJson instanceof Collection) {
                    ((Collection<?>) fromJson).forEach(o -> {
                        if (o instanceof Macro macro) {
                            macro.setHasOp(false);
                        }
                    });
                }
            }
            this.fileMacros.addAll((Collection<? extends Macro>) fromJson);

        } catch (IOException e) {
            CommandMacroKey.LOGGER.error("Failed to load macros", e);
        }
    }

    private Type getMacroListType() {
        return new TypeToken<List<Macro>>(){}.getType();
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String json = GSON.toJson(this.fileMacros);
            FileUtils.writeStringToFile(file, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            CommandMacroKey.LOGGER.error("Failed to save macros", e);
        }
    }
    public List<Macro> getFileMacros() {
        return fileMacros;
    }

    static class MacroDeserializer implements JsonDeserializer<Macro> {
        @Override
        public Macro deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Macro.CODEC.get().parse(JsonOps.INSTANCE, json).getOrThrow(false, CommandMacroKey.LOGGER::error);
        }
    }
}

