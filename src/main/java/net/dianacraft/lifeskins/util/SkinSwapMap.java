package net.dianacraft.lifeskins.util;

import net.dianacraft.lifeskins.command.LifeSkinsCommand;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class SkinSwapMap {
    private static Map<String, String> usernameMap = new HashMap<>();

    public static void reset(){
        usernameMap = new HashMap<>();
    }

    public static void force(String from, String to){
        usernameMap.put(from, to);
        ServerPlayerEntity player = PlayerUtils.getPlayer(from);
        if (player != null){
            LifeSkinsCommand.reloadSkin(player);
        }
    }

    public static int swap(String player1, String player2){
        if (usernameMap.containsKey(player1) && usernameMap.containsKey(player2)){
            usernameMap.put(player1, player2);
            usernameMap.put(player2, player1);

            ServerPlayerEntity serverPlayer1 = PlayerUtils.getPlayer(player1);
            if (serverPlayer1 != null){
                LifeSkinsCommand.reloadSkin(serverPlayer1);
            }
            ServerPlayerEntity serverPlayer2 = PlayerUtils.getPlayer(player2);
            if (serverPlayer2 != null){
                LifeSkinsCommand.reloadSkin(serverPlayer2);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static void addDefault(String player){
        usernameMap.put(player, player);
    }

    public static void add(String player){
        if (usernameMap.containsKey(player)){
            addDefault(player);
        }
    }

    public static String get(String player){
        return usernameMap.getOrDefault(player, player);
    }
}
