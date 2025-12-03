package net.dianacraft.lifeskins.mixin;

import net.dianacraft.lifeskins.command.LifeSkinsCommand;
import net.dianacraft.lifeskins.util.Skin;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {
    @Inject(method = "addScoreHolderToTeam", at=@At("HEAD"))
    public void addScoreHolderToTeam(String scoreHolderName, Team team, CallbackInfoReturnable<Boolean> cir){
        ServerPlayerEntity player = PlayerUtils.getPlayer(scoreHolderName);
        if (player == null) return;
        if (player.getScoreboardTeam() == null) return;
        SkinPathFinder spf = new SkinPathFinder(player);

        if (!player.getScoreboardTeam().getName().equals(player.getScoreboardTeam().getName()) && spf.hasSkins() && player.isAlive()){
            Skin skin = spf.getSkin(team.getName());
            if (skin == null) skin = spf.getSkin();
            LifeSkinsCommand.reloadSkin(player, skin);
        }
    }
}
