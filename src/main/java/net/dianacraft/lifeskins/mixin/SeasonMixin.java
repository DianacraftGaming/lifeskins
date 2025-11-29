package net.dianacraft.lifeskins.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.mat0u5.lifeseries.seasons.boogeyman.BoogeymanManager;
import net.mat0u5.lifeseries.seasons.other.LivesManager;
import net.mat0u5.lifeseries.seasons.season.Season;
import net.mat0u5.lifeseries.seasons.secretsociety.SecretSociety;
import net.mat0u5.lifeseries.utils.player.PermissionManager;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = Season.class, remap = false)
public class SeasonMixin {
    @Shadow private long ticks = 0L;
    @Shadow public BoogeymanManager boogeymanManager = this.createBoogeymanManager();
    @Shadow public SecretSociety secretSociety = this.createSecretSociety();

    @Shadow public BoogeymanManager createBoogeymanManager() {
        return new BoogeymanManager();
    }
    @Shadow public SecretSociety createSecretSociety() {
        return new SecretSociety();
    }

    /**
     * @author DianacraftGaming
     * @reason Deleted the refresh
     */
    @Overwrite
    public void tick(MinecraftServer server) {
        ++this.ticks;
        this.boogeymanManager.tick();
        this.secretSociety.tick();
    }
}