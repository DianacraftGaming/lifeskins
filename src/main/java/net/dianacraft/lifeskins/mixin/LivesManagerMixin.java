package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.mat0u5.lifeseries.seasons.other.LivesManager;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.samo_lego.fabrictailor.command.SkinCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dianacraft.lifeskins.LifeSkins.LOGGER;
import static net.dianacraft.lifeskins.command.LifeSkinsCommand.*;
import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.seasons.other.WatcherManager.isWatcher;
import static org.samo_lego.fabrictailor.util.SkinFetcher.fetchSkinByName;

@Mixin(LivesManager.class)
public abstract class LivesManagerMixin {

    @Shadow public abstract @Nullable Integer getPlayerLives(ServerPlayerEntity player);

    @Inject(method = "resetPlayerLife", at = @At("HEAD"))
    private void resetPlayerLife(ServerPlayerEntity player, CallbackInfo ci) {
        SkinPathFinder spf = new SkinPathFinder(player.getNameForScoreboard());
        if (spf.hasSkins() && player.isAlive()){
            //File f = new File(spf.getDirectoryPath()+"/default.png");
            //if(f.isFile()){
            //    SkinCommand.setSkin(player, () -> setSkinFromFile(spf.getDirectoryPath()+"/default.png", SkinPathFinder.getSlim(spf.getSkin(Main.currentSeries.getPlayerLives(player)))));
            //} else {
            SkinCommand.setSkin(player, () -> fetchSkinByName(player.getName().getString()));
            //}
        }

    }

    @Inject(method = "setPlayerLives", at = @At("HEAD"))
    public void setPlayerLives(ServerPlayerEntity player, int lives, CallbackInfo ci){

        if (player == null) return;
        if (isWatcher(player)) return;
        int prevLives = -1;
        if (getPlayerLives(player) != null) prevLives = getPlayerLives(player);
        if (lives != prevLives){
            try {
                reloadSkinSubin(player, lives);
            } catch (CommandSyntaxException ignored) {}
        }
    }
}