package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.command.LifeSkinsCommand;
import net.mat0u5.lifeseries.seasons.season.wildlife.wildcards.wildcard.superpowers.Superpowers;
import net.mat0u5.lifeseries.seasons.season.wildlife.wildcards.wildcard.superpowers.ToggleableSuperpower;
import net.mat0u5.lifeseries.seasons.season.wildlife.wildcards.wildcard.superpowers.superpower.PlayerDisguise;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerDisguise.class)
public class PlayerDisguiseMixin extends ToggleableSuperpower {

    // Doesn't appear to be necessary, however I will keep the file in case I need it later

    public PlayerDisguiseMixin(ServerPlayerEntity player) {
        super(player);
    }

    public Superpowers getSuperpower() {
        return Superpowers.PLAYER_DISGUISE;
    }

    @Inject(method = "activate", at = @At("TAIL"), remap = false)
    public void activate(CallbackInfo ci){
        ServerPlayerEntity player = getPlayer();
        if (player == null) return;
        Entity lookingAt = PlayerUtils.getEntityLookingAt(player, 50);
        if (lookingAt != null)  {
            if (lookingAt instanceof ServerPlayerEntity lookingAtPlayer) {
                lookingAtPlayer = PlayerUtils.getPlayerOrProjection(lookingAtPlayer);
                if (!PlayerUtils.isFakePlayer(lookingAtPlayer)) {
                    LifeSkinsCommand.stealSkin(player, lookingAtPlayer.getNameForScoreboard());
                }
            }
        }
    }

    @Inject(method = "deactivate", at = @At("TAIL"), remap = false)
    public void deactivate(CallbackInfo ci) throws CommandSyntaxException {
        LifeSkinsCommand.reloadSkinSubin(getPlayer());
    }
}
