package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.lifeseries.seasons.other.LivesManager;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.mat0u5.lifeseries.seasons.season.limitedlife.LimitedLifeLivesManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dianacraft.lifeskins.command.LifeSkinsCommand.*;
import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.seasons.other.WatcherManager.isWatcher;

@Mixin(LimitedLifeLivesManager.class)
public abstract class LimitedLivesManagerMixin extends LivesManager {

    @Inject(method = "setPlayerLives", at = @At("HEAD"))
    public void setPlayerLives(ServerPlayerEntity player, int lives, CallbackInfo ci){
        if (player == null) return;
        if (isWatcher(player)) return;
        int prevLives = -1;
        if (getPlayerLives(player) != null) prevLives = getPlayerLives(player);
        if (getLivesForLimited(lives) != getLivesForLimited(prevLives)){
            reloadSkin(player, lives);
        }
    }
}