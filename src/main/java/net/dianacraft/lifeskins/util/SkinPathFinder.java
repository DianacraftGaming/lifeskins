package net.dianacraft.lifeskins.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static net.dianacraft.lifeskins.LifeSkins.MOD_ID;

public class SkinPathFinder {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    SkinFile skinFile;
    Skin[] skins;
    ServerPlayerEntity player;
    String directoryPath = "config/lifeskins/Player";

    public SkinPathFinder(ServerPlayerEntity playerEntity){
        player = playerEntity;
        directoryPath = "config/lifeskins/"+"Player";//+player.getName().getString(); //TODO: Username here

        SkinFile defaultSkinFile = new SkinFile(false);
        Gson gson = new Gson();
        try (Reader reader = new FileReader(directoryPath + "/skins.json")) {
            skinFile = gson.fromJson(reader, SkinFile.class);
        } catch (IOException e) {
            if (new File(directoryPath).mkdir()){
                LOGGER.info("Created a life skins folder for "+player.getName().getString()); //TODO: Username here
            }
            try (Writer writer = new FileWriter(directoryPath + "/skins.json")) {
                gson.toJson(defaultSkinFile, writer);
                skinFile = defaultSkinFile;
            } catch (IOException f) {
                throw new RuntimeException(f);
            }
        }
        skins = skinFile.getSkinArray();
    }

    public boolean hasSkins(){
        if (skins == null) {
            return false; // Check skins in the folder
        } else {
            return true;
        }
    }

    public @NotNull String getSkinPath(){ // Read from skins.json and get the skin path
        String username = "Player"; //player.getName().getString(); //"Player"; //TODO: Username here
        String skinName = "2.png"; //read json, if no skin - currentSeries.getPlayerLives(self)

        return "config/lifeskins/"+username+"/"+skinName;
    }

    public static boolean getSlim(Skin skin){
        return skin.getSlim();
    }

    private Skin[] findSkins(){
        String username = "Player"; //player.getName().getString(); //"Player"; //TODO: Username here

        return null;
    }

    public int logSkins(){
        player.sendMessage(Text.of("§e| Your skins:"));

        player.sendMessage(Text.of("- none"));
        return 1;
    }
    //TODO: log skin list method
}
