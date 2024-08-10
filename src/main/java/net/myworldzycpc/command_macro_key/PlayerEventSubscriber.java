package net.myworldzycpc.command_macro_key;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }

//        \%([^%]+)\%
//        \&([^&]+)\&
        String command = "/tp %player_name% %player_pos_x% %player_pos_y% %player_pos_z%";
        Pattern pattern = Pattern.compile("\\%([^%]+)\\%");
        //提取出所有的占位符
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            String group = matcher.group();
            String placeholder = group.substring(1, group.length() - 1);

        }


    }
}