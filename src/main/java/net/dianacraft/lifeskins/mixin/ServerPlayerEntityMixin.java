package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dianacraft.lifeskins.command.LifeSkinsCommand.getLivesForLimited;
import static net.dianacraft.lifeskins.command.LifeSkinsCommand.reloadSkin;
import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.Main.livesManager;
import static net.mat0u5.lifeseries.seasons.season.Seasons.LIMITED_LIFE;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onSpawn", at = @At("HEAD"))
    public void onSpawn(CallbackInfo ci) {
        if (currentSeason.getSeason() == Seasons.UNASSIGNED) return;
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        if (!livesManager.hasAssignedLives(player)) return;

        try {
            if (currentSeason.getSeason() == LIMITED_LIFE){
                reloadSkin(player, getLivesForLimited(player));
            } else {
                reloadSkin(player);
            }
        } catch (CommandSyntaxException ignored) {}
    }
}
