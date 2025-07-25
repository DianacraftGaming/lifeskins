package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.mat0u5.lifeseries.seasons.season.Season;
import net.minecraft.server.network.ServerPlayerEntity;
import org.samo_lego.fabrictailor.command.SkinCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dianacraft.lifeskins.LifeSkins.LOGGER;
import static net.dianacraft.lifeskins.command.LifeSkinsCommand.reloadSkin;
import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.seasons.season.Seasons.LIMITED_LIFE;
import static net.mat0u5.lifeseries.seasons.season.limitedlife.LimitedLife.*;
import static org.samo_lego.fabrictailor.util.SkinFetcher.fetchSkinByName;

@Mixin(Season.class)
public class SeasonMixin {

    @Inject(method = "setPlayerLives", at = @At("HEAD"))
    private void setPlayerLives(ServerPlayerEntity player, int lives, CallbackInfo ci) {
        try {
            //LifeSkinsCommand.reloadSkin(player, lives);
            //TaskScheduler.scheduleTask(40, );// TODO; see if i can only have the delay on death
            if (player.isAlive()) {
                reloadSkin(player,lives);
            }
        } catch (CommandSyntaxException e) {
            LOGGER.info("Somethibg went wrong idk when this might even appear");
        }
    }

    @Inject(method = "reloadPlayerTeam", at = @At("HEAD"))
    private void reloadPlayerTeam(ServerPlayerEntity player, CallbackInfo ci) {

        if(currentSeason.getSeason() == LIMITED_LIFE){
            try {
                Integer lives = currentSeason.getPlayerLives(player);
                if (lives == null) {
                    return;
                } else if (lives <= 0) {
                    reloadSkin(player, 0);
                } else if (lives > YELLOW_TIME) {
                    reloadSkin(player, 3);
                } else if (lives > RED_TIME) {
                    reloadSkin(player, 2);
                } else  {
                    reloadSkin(player, 1);
                }

            } catch (CommandSyntaxException e) {
                LOGGER.info("Somethibg went wrong idk when this might even appear");
            }
        }
    }


    @Inject(method = "resetPlayerLife", at = @At("HEAD"))
    private void resetPlayerLife(ServerPlayerEntity player, CallbackInfo ci) {
        SkinPathFinder spf = new SkinPathFinder(player);
        if (spf.hasSkins()){
            //File f = new File(spf.getDirectoryPath()+"/default.png");
            //if(f.isFile()){
            //    SkinCommand.setSkin(player, () -> setSkinFromFile(spf.getDirectoryPath()+"/default.png", SkinPathFinder.getSlim(spf.getSkin(Main.currentSeries.getPlayerLives(player)))));
            //} else {
                SkinCommand.setSkin(player, () -> fetchSkinByName(player.getName().getString()));
            //}
        }

    }

    @Inject(method = "onPlayerRespawn", at = @At("HEAD"))
    public void onPlayerRespawn(ServerPlayerEntity player, CallbackInfo ci) {
        try {
            reloadSkin(player, currentSeason.getPlayerLives(player));
        } catch (CommandSyntaxException e) {
            //throw new RuntimeException(e);
        }
    }

}
