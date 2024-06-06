package com.xiaohunao.command_macro_key;

import com.google.common.collect.Maps;
import net.minecraft.client.player.LocalPlayer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholder {
    private static final Map<String, PlaceholderReplacement> placeholderMap = Maps.newHashMap();
    //替换占位符
    public static String replacePlaceholder(String command, LocalPlayer player) {
        Pattern pattern = Pattern.compile("\\%([^%]+)\\%");
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            String group = matcher.group();
            String placeholder = group.substring(1, group.length() - 1);
            PlaceholderReplacement replacement = placeholderMap.get(placeholder);
            if (replacement != null) {
                String replacementString = replacement.replace(player);
                command = command.replace(group, replacementString);
            }
        }
        return command;
    }

    public static void init() {
        register("player_name", player -> player.getGameProfile().getName());
        register("player_pos_x", player -> String.valueOf(player.getX()));
        register("player_pos_y", player -> String.valueOf(player.getY()));
        register("player_pos_z", player -> String.valueOf(player.getZ()));
        register("player_pos", player -> String.format("%s %s %s", player.getX(), player.getY(), player.getZ()));
        register("player_health", player -> String.valueOf(player.getHealth()));
        register("player_food", player -> String.valueOf(player.getFoodData().getFoodLevel()));
        register("player_exp", player -> String.valueOf(player.experienceProgress));
        register("player_level", player -> String.valueOf(player.experienceLevel));
        register("player_dimension", player -> player.level().dimension().location().toString());
        register("player_biome", player -> player.level().getBiome(player.blockPosition()).get().toString());
        register("player_facing", player -> player.getDirection().toString());
        register("player_xRot", player -> String.valueOf(player.getXRot()));
        register("player_yRot", player -> String.valueOf(player.getYRot()));
        register("player_uuid", player -> player.getGameProfile().getId().toString());

    }

    public static void register(String placeholder, PlaceholderReplacement replacement) {
        placeholderMap.put(placeholder, replacement);
    }

    public interface PlaceholderReplacement {
        String replace(LocalPlayer player);
    }
}
