package net.dianacraft.lifeskins.util;

import net.mat0u5.lifeseries.Main;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.mat0u5.lifeseries.seasons.subin.SubInManager;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.google.gson.*;

import java.io.*;
import java.util.*;

import static net.dianacraft.lifeskins.LifeSkins.LOGGER;
import static net.dianacraft.lifeskins.command.LifeSkinsCommand.getLivesForLimited;
import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.seasons.season.Seasons.LIMITED_LIFE;

public class SkinPathFinder {
    SkinFile skinFile;
    List<Skin> skins;
    List<Skin> teamSkins = new ArrayList<>();
    Map<Integer, Skin> livesMap = new HashMap<>();
    Map<String, Skin> teamMap = new HashMap<>();
    private int lowest = 50;
    private int highest = -50; //placeholder values
    ServerPlayerEntity player;
    String playerUsername;
    String directoryPath;
    //Skin defaultSkin;

    public SkinPathFinder(ServerPlayerEntity playerEntity){
        player = playerEntity;
        if (SubInManager.isSubbingIn(playerEntity.getUuid())){
            playerUsername = OtherUtils.profileName(SubInManager.getSubstitutedPlayer(playerEntity.getUuid()));
        } else {
            playerUsername = playerEntity.getNameForScoreboard();
        }
        defaults();
    }

    public SkinPathFinder(ServerPlayerEntity playerEntity, String subIn){
        player = playerEntity;
        playerUsername = subIn;
        defaults();
    }

    private void defaults(){
        directoryPath = "config/lifeskins/"+playerUsername;

        skinFile = new SkinFile(false);
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(directoryPath + "/skins.json");
            skinFile = gson.fromJson(reader, SkinFile.class);
            reader.close();
        } catch (IOException e) {
            if (new File(directoryPath).mkdir()){
                LOGGER.info("Created a life skins folder for {}", playerUsername);
            }
            try {
                Writer writer = new FileWriter(directoryPath + "/skins.json");
                gson.toJson(skinFile, writer);
                writer.flush();
                writer.close();
            } catch (Exception f) {
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("Failed to initialize a skins.json file for " + playerUsername);
            }
        }
        if (skinFile.getSkinArray() == null) {
            skins = new SkinFile(false).getSkinArray();
        } else {
            skins = new ArrayList<>(skinFile.getSkinArray());
            teamSkins = new ArrayList<>(skinFile.getSkinArray());
            skins.removeIf(skin -> skin.getLifeCount() == null);

            teamSkins = skinFile.getSkinArray();
            teamSkins.removeIf(skin -> skin.getTeam() == null);
        }
        livesMap = initializeSkinMap();
        teamMap = initialiseTeamMap();
    }

    public boolean hasSkins(){
        if (skins.isEmpty()) {
            //return false; // Check skins in the folder
            livesMap = initializeSkinMap();
        }
        return !skins.isEmpty() || !teamSkins.isEmpty();
    }

    public Skin getSkin(){
        Skin skin;
        if (player.getScoreboardTeam() != null){
            skin = getSkin(player.getScoreboardTeam().getName());
            if (skin != null) return skin;
        }

        Integer playerLives = Main.livesManager.getPlayerLives(player);
        if (playerLives == null) return null;
        if (currentSeason.getSeason() == LIMITED_LIFE) playerLives = getLivesForLimited(playerLives);
        return getSkin(playerLives);
    }

    public Skin getSkin(int lives){
        return getMapSkin(lives);
    }

    public Skin getSkin(String team){
        if (player != null) {
            if (team != null) {
                if (teamMap.containsKey(team)) {
                    return teamMap.get(team);
                }
            }
        }
        return null;
    }

    public String getSkinPath(Skin skin){
        return directoryPath+"/"+skin.getName();
    }

    public Skin getMapSkin(Integer lives){
        if (livesMap.isEmpty()) return null;
        if (livesMap.containsKey(lives))
            return livesMap.get(lives);
        else {
            if (lives < lowest)
                return livesMap.get(lowest);
            if (lives > highest)
                return livesMap.get(highest);
        }
        return null; //should never get here
    }

    private ArrayList<Skin> findSkins(){
        ArrayList<Skin> skinArray = new ArrayList<>();
        String[] allFiles = new File(directoryPath).list();
        if (allFiles == null) return skinArray;
        for (String file : allFiles) {
            String[] tmp = file.split("\\.");
            if (tmp.length == 2) {
                if (Objects.equals(tmp[1], "png")) {
                    if (tmp[0].matches("-?(0|[1-9]\\d*)")) {
                        Skin skin = new Skin(tmp[0] + ".png", Integer.parseInt(tmp[0]), skinFile.getSlim());
                        skinArray.add(skin);
                    }
                }
            }
        }
        return skinArray;
    }

    private Map<Integer, Skin> initializeSkinMap(){
        Map<Integer, Skin> map = new HashMap<>();
        if (skins.isEmpty()) skins = findSkins();
        for (Skin skin : skins) {
            if (skin.getLifeCount() > highest) {
                highest = skin.getLifeCount();
            }
            if (skin.getLifeCount() < lowest) {
                lowest = skin.getLifeCount();
            }
        }
        Skin currentskin = new Skin("", -51);
        for (int i = lowest; i <= highest; i++) {
            boolean hasskin = false;
            for (Skin skin: skins){
                if (skin.getLifeCount() == i) {
                    hasskin = true;
                    currentskin = skin;
                    map.put(i, skin);
                }
            }
            if (!hasskin) {
                map.put(i, currentskin/*.setLifeCount(i)*/);
            }
        }
        return map;
    }

    public Map<String, Skin> initialiseTeamMap(){
        Map<String, Skin> skinMap = new HashMap<>();
        if (teamSkins.isEmpty()){
            return skinMap;
        }
        for (Skin skin:teamSkins){
            skinMap.put(skin.getTeam(), skin);
        }
        return skinMap;
    }

    public int logSkins(){
        if (player == null) return -1;

        if (SubInManager.isSubbingIn(player.getUuid())){
            player.sendMessage(Text.of("§e| "+playerUsername+"'s skins:"));
        } else {
            player.sendMessage(Text.of("§e| Your skins:"));
        }

        if (hasSkins()){

            /*
            for (int i = lowest; i <= highest; i++) {
                String message = String.valueOf(i);
                if (message.equals("1"))
                    player.sendMessage(Text.of(message + " Life: §b"+skinMap.get(i).getName()));
                else
                    player.sendMessage(Text.of(message + " Lives: §b"+skinMap.get(i).getName()));
                //skins.get(i).getLifeCount();
            }*/ //THE FALLBACK IN CASE ANYTHING GOES WRONG

            // Team skins

            if (!teamSkins.isEmpty()){
                player.sendMessage(Text.of("\n| Team Skins:"));
                for (Skin skin : teamSkins){
                    player.sendMessage(Text.of(skin.getTeam() + ": §b"+skin.getName()));
                }
                player.sendMessage(Text.of("\n| Life Skins:"));
            }


            // Life skins
            if (!livesMap.isEmpty()) {
                for (int i = lowest; i <= highest; i++) {
                    String message = "";

                    if (i < highest)
                        if (livesMap.get(i).equals(livesMap.get(i + 1)))
                            continue;
                    if (livesMap.get(i).getLifeCount() == i) {
                        message += i;
                    } else {
                        message += livesMap.get(i).getLifeCount() + "-" + i;
                    }
                    if (i == highest) {
                        message += "+";
                    }

                    if (message.equals("1"))
                        player.sendMessage(Text.of(message + " Life: §b" + livesMap.get(i).getName()));
                    else
                        player.sendMessage(Text.of(message + " Lives: §b" + livesMap.get(i).getName()));
                }
            } else {
                player.sendMessage(Text.of("- none found. It is recommended but not required to\n have at least 1 life skin as a fallback"));
            }
        } else {
            player.sendMessage(Text.of("- no skins found, run \"/lifeskins info\" to get setup instructions."));
        }
        return 1;
    }

    public Skin getSkin(ServerPlayerEntity player){
        SkinPathFinder spf = new SkinPathFinder(player);
        if (spf.hasSkins()) {
            if (currentSeason.getSeason() == LIMITED_LIFE) {
                return spf.getSkin(getLivesForLimited(player));
            } else {
                return spf.getSkin();
            }
        }

        return null;
    }

    public Skin getSkin(ServerPlayerEntity player, int lives){
        SkinPathFinder spf = new SkinPathFinder(player);
        if (spf.hasSkins()) {
            if (currentSeason.getSeason() == LIMITED_LIFE) {
                return spf.getSkin(getLivesForLimited(lives));
            } else {
                return spf.getSkin(lives);
            }
        }
        return null;
    }
}
