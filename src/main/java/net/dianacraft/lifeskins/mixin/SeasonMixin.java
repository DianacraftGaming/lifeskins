package net.dianacraft.lifeskins.mixin;

import net.mat0u5.lifeseries.seasons.boogeyman.BoogeymanManager;
import net.mat0u5.lifeseries.seasons.season.Season;
import net.mat0u5.lifeseries.seasons.secretsociety.SecretSociety;
import net.minecraft.server.MinecraftServer;
import net.mat0u5.lifeseries.utils.other.Time;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = Season.class, remap = false)
public class SeasonMixin {
    @Shadow private Time timer = Time.zero();
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
        timer.tick();
        this.boogeymanManager.tick();
        this.secretSociety.tick();
    }
}