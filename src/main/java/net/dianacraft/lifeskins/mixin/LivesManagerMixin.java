package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.mat0u5.lifeseries.seasons.other.LivesManager;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.mat0u5.lifeseries.seasons.subin.SubInManager;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
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
        SkinPathFinder spf = new SkinPathFinder(player);
        if (spf.hasSkins() && player.isAlive()){
            SkinCommand.setSkin(player, () -> fetchSkinByName(player.getName().getString()));
        }

    }
}