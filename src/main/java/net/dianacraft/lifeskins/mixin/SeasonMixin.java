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
import static net.mat0u5.lifeseries.Main.livesManager;
import static net.mat0u5.lifeseries.seasons.season.Seasons.LIMITED_LIFE;
import static net.mat0u5.lifeseries.seasons.season.limitedlife.LimitedLifeLivesManager.*;
import static org.samo_lego.fabrictailor.util.SkinFetcher.fetchSkinByName;

@Mixin(Season.class)
public class SeasonMixin {

    @Inject(method = "reloadPlayerTeam", at = @At("HEAD"))
    private void reloadPlayerTeam(ServerPlayerEntity player, CallbackInfo ci) {

        if(currentSeason.getSeason() == LIMITED_LIFE){
            try {
                Integer lives = livesManager.getPlayerLives(player);
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

    @Inject(method = "onPlayerRespawn", at = @At("HEAD"))
    public void onPlayerRespawn(ServerPlayerEntity player, CallbackInfo ci) {
        try {
            reloadSkin(player, livesManager.getPlayerLives(player));
        } catch (CommandSyntaxException e) {
            //throw new RuntimeException(e);
        }
    }

}
