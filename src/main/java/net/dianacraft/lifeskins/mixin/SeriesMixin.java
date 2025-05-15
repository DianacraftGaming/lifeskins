package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.command.LifeSkinsCommand;
import net.mat0u5.lifeseries.series.Series;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dianacraft.lifeskins.LifeSkins.LOGGER;

@Mixin(Series.class)
public class SeriesMixin {

    @Inject(method = "setPlayerLives", at = @At("TAIL"))
    private void injectMethod(ServerPlayerEntity player, int lives, CallbackInfo ci) {
        try {
            LifeSkinsCommand.reloadSkin(player, lives);
        } catch (CommandSyntaxException e) {
            LOGGER.info("Somethibg went wrong idk when this might even appear");
        }
    }
}
