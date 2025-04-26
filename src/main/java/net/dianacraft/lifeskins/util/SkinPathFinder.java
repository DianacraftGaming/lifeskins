package net.dianacraft.lifeskins.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SkinPathFinder {
    public static boolean hasSkins(ServerPlayerEntity player){
        return false; // Check if skins.json is present
    };
    public static String getSkinPath(ServerPlayerEntity player){ // Read from skins.json and get the skin path
        String username = "Player";
        String skinName = "2.png"; //currentSeries.getPlayerLives(self)

        return "config/lifeskins/"+username+"/"+skinName;
    }
    public static int logSkins(ServerPlayerEntity player){
        player.sendMessage(Text.of("§e| Your skins:"));
        player.sendMessage(Text.of("- none"));
        return 1;
    }
    //TODO: log skin list method
}
