package net.dianacraft.lifeskins.util;

import net.mat0u5.lifeseries.Main;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static net.dianacraft.lifeskins.LifeSkins.MOD_ID;

public class SkinPathFinder {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    SkinFile skinFile;
    List<Skin> skins;
    Map<Integer, Skin> skinMap;
    private int lowest = 50;
    private int highest = -50; //placeholder values
    ServerPlayerEntity player;
    String directoryPath;

    public SkinPathFinder(ServerPlayerEntity playerEntity){
        player = playerEntity;
        directoryPath = "config/lifeskins/"+player.getName().getString();

        SkinFile defaultSkinFile = new SkinFile(false);
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(directoryPath + "/skins.json");
            skinFile = gson.fromJson(reader, SkinFile.class);
        } catch (IOException e) {
            if (new File(directoryPath).mkdir()){
                LOGGER.info("Created a life skins folder for "+player.getName().getString());
            }
            try (Writer writer = new FileWriter(directoryPath + "/skins.json")) {
                gson.toJson(defaultSkinFile, writer);
                skinFile = defaultSkinFile;
            } catch (IOException f) {
                throw new RuntimeException(f);
            }
        }
        skins = skinFile.getSkinArray();
        skinMap = initializeSkinMap();
    }

    public boolean hasSkins(){
        if (skins.isEmpty()) {
            //return false; // Check skins in the folder
            skinMap = initializeSkinMap();
            return !skins.isEmpty();
        } else {
            return true;
        }
    }

    public String getSkinPath(){ // Read from skins.json and get the skin path
        int playerLives = 0;
        if (Main.currentSeries.hasAssignedLives(player)) {
            playerLives = Main.currentSeries.getPlayerLives(player);
        }
        return directoryPath+"/"+getSkin(playerLives).getName();
    }

    public Skin getSkin(Integer lives){
        if (skinMap.containsKey(lives))
            return skinMap.get(lives);
        else {
            if (lives < lowest)
                return skinMap.get(lowest);
            if (lives > highest)
                return skinMap.get(highest);
        }
        return null; //should never get here
    }

    public static boolean getSlim(Skin skin){
        return skin.getSlim();
    }

    private ArrayList<Skin> findSkins(){
        //String username = "Player"; //player.getName().getString(); //"Player"; //TODO: Username here
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
        if (skins == null) skins = findSkins();
        for (Skin skin : skins) {
            if (skin.getLifeCount() > highest) {
                highest = skin.getLifeCount();
            }
            if (skin.getLifeCount() < lowest) {
                lowest = skin.getLifeCount();
            }
        }
        Skin currentskin = new Skin("", 0);
        for (int i = highest; i >= lowest; i--) {
            boolean hasskin = false;
            for (Skin skin : skins) {
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

    public int logSkins(){
        player.sendMessage(Text.of("§e| Your skins:"));
        if (hasSkins()){
            for (int i = lowest; i <= highest; i++) {
                String message = "";
                if (skinMap.get(i).getLifeCount() > i){
                    message += i + "-" + skinMap.get(i).getLifeCount();
                    i = skinMap.get(i).getLifeCount();
                } else {
                    message += i;
                }

                if (message.equals("1"))
                    player.sendMessage(Text.of(message + " Life: §b"+skinMap.get(i).getName()));
                else
                    player.sendMessage(Text.of(message + " Lives: §b"+skinMap.get(i).getName()));
                //skins.get(i).getLifeCount();
            }
        } else {
            player.sendMessage(Text.of("- none"));
        }
        return 1;
    }
    //TODO: log skin list method


}
