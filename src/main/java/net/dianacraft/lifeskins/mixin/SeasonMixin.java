package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.mat0u5.lifeseries.seasons.season.Season;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.samo_lego.fabrictailor.command.SkinCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dianacraft.lifeskins.LifeSkins.LOGGER;
import static net.dianacraft.lifeskins.command.LifeSkinsCommand.getLivesForLimited;
import static net.dianacraft.lifeskins.command.LifeSkinsCommand.reloadSkin;
import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.Main.livesManager;
import static net.mat0u5.lifeseries.seasons.season.Seasons.LIMITED_LIFE;
import static net.mat0u5.lifeseries.seasons.season.limitedlife.LimitedLifeLivesManager.*;
import static org.samo_lego.fabrictailor.util.SkinFetcher.fetchSkinByName;

@Mixin(Season.class)
public class SeasonMixin {

    @Inject(method = "reloadPlayerTeamActual", at = @At("HEAD"))
    private void reloadPlayerTeamActual(ServerPlayerEntity player, CallbackInfo ci) throws CommandSyntaxException {
        if (currentSeason.getSeason() == LIMITED_LIFE){
            reloadSkin(player, getLivesForLimited(player));
        } else {
            reloadSkin(player);
        }
    }

}
