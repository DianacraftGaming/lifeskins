package net.dianacraft.lifeskins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.util.Skin;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.mat0u5.lifeseries.seasons.subin.SubInManager;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.samo_lego.fabrictailor.command.SkinCommand;

import java.util.Collection;

import static net.mat0u5.lifeseries.Main.*;
import static net.mat0u5.lifeseries.seasons.season.Seasons.LIMITED_LIFE;
import static net.mat0u5.lifeseries.seasons.season.limitedlife.LimitedLifeLivesManager.*;
import static net.mat0u5.lifeseries.utils.player.PermissionManager.isAdmin;
import static net.minecraft.server.command.CommandManager.literal;
import static org.samo_lego.fabrictailor.util.SkinFetcher.fetchSkinByName;
import static org.samo_lego.fabrictailor.util.SkinFetcher.setSkinFromFile;

public class LifeSkinsCommand {

    private static int contextReloadSkin(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        SkinPathFinder spf = new SkinPathFinder(player.getNameForScoreboard());
        if (!livesManager.hasAssignedLives(player)) {
            player.sendMessage(Text.of("You have not been assigned any lives yet."));
            return -1;
        } else if (!spf.hasSkins() && !SubInManager.isSubbingIn(player.getUuid())) {
            player.sendMessage(Text.of("§cCouldn't find any life skins! Make sure you set them up correctly, run \"/lifeskins setup\" to get setup instructions"), false);
            return -1;
        }
        return reloadSkinSubin(player);
    }

    public static int reloadSkin(ServerPlayerEntity player) {
        SkinPathFinder spf = new SkinPathFinder(player.getNameForScoreboard());
        Skin skin;
        if (currentSeason.getSeason() == LIMITED_LIFE) skin = spf.getSkin(getLivesForLimited(player));
        else skin = spf.getSkin();
        if (skin == null) {
            SkinCommand.setSkin(player, () -> fetchSkinByName(player.getNameForScoreboard()));
            return -1;
        }
        SkinCommand.setSkin(player, () -> setSkinFromFile(spf.getSkinPath(skin), skin.getSlim()));
        return 1;
    }

    public static void reloadSkin(ServerPlayerEntity player, int lives) {
        SkinPathFinder spf = new SkinPathFinder(player.getNameForScoreboard());
        Skin skin;
        if (currentSeason.getSeason() == LIMITED_LIFE) skin = spf.getSkin(getLivesForLimited(lives));
        else skin = spf.getSkin(lives);
        if (skin == null) {
            SkinCommand.setSkin(player, () -> fetchSkinByName(player.getNameForScoreboard()));
            return;
        }
        SkinCommand.setSkin(player, () -> setSkinFromFile(spf.getSkinPath(skin), skin.getSlim()));
    }

    public static void stealSkin(ServerPlayerEntity actor, String target){
        SkinPathFinder spf = new SkinPathFinder(target);
        Skin skin = spf.getSkin();
        if (skin == null){
            SkinCommand.setSkin(actor, () -> fetchSkinByName(target));
        } else {
            SkinCommand.setSkin(actor, () -> setSkinFromFile(spf.getSkinPath(skin), skin.getSlim()));
        }
    }

    public static void stealSkin(ServerPlayerEntity actor, String target, int lives){
        SkinPathFinder spf = new SkinPathFinder(target);
        Skin skin = spf.getSkin(lives);
        if (skin == null){
            SkinCommand.setSkin(actor, () -> fetchSkinByName(target));
        } else {
            SkinCommand.setSkin(actor, () -> setSkinFromFile(spf.getSkinPath(skin), skin.getSlim()));
        }
    }

    public static int reloadSkinSubin(ServerPlayerEntity player) throws CommandSyntaxException {
        if (SubInManager.isSubbingIn(player.getUuid())){
            stealSkin(player, OtherUtils.profileName(SubInManager.getSubstitutedPlayer(player.getUuid())));
            return 1;
        } else {
            return reloadSkin(player);
        }
    }

    public static void reloadSkinSubin(ServerPlayerEntity player, int lives) throws CommandSyntaxException {
        if (SubInManager.isSubbingIn(player.getUuid())){
            stealSkin(player, OtherUtils.profileName(SubInManager.getSubstitutedPlayer(player.getUuid())), lives);
        } else {
            reloadSkin(player, lives);
        }
    }

    public static int getLivesForLimited(ServerPlayerEntity player){
        if(livesManager.hasAssignedLives(player)){
            Integer lives = livesManager.getPlayerLives(player);
            if (lives == null) {
                return 0;
            } else {
                return getLivesForLimited(lives);
            }
        }
        return 0;
    }

    public static int getLivesForLimited(Integer time){
        if (time <= 0) {
            return 0;
        } else if (time <= RED_TIME) {
            return 1;
        } else if (time <= YELLOW_TIME) {
            return 2;
        } else {
            return time <= DEFAULT_TIME ? 3 : 4;
        }
    }

    public static int reloadSkinFor(Collection<ServerPlayerEntity> players){
        for (ServerPlayerEntity player : players){
            try {
                reloadSkinSubin(player);
            } catch (CommandSyntaxException ignored) {}
        }
        return 0;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("lifeskins")
                        .then(literal("reload")
                                .executes(LifeSkinsCommand::contextReloadSkin)
                                .then(CommandManager.argument("player", EntityArgumentType.players()).requires(source -> (isAdmin(source.getPlayer()) || (source.getEntity() == null))).executes((context) -> {return reloadSkinFor(EntityArgumentType.getPlayers(context, "player"));}))
                        )/*
                        .then(literal("stealSkin")
                                .requires(PermissionManager::isAdmin)
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context ->
                                            {
                                                stealSkin(context.getSource().getPlayerOrThrow(), EntityArgumentType.getPlayer(context, "player"));
                                                return 1;
                                            }
                                        )
                                )
                        )*/
                        .then(literal("setup")
                                .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                            if (player != null) {
                                                player.sendMessage(Text.of("§e| Skin setup info:"));
                                                player.sendMessage(Text.of("In the config folder create a folder with your username and put the skins you want to use inside!"));
                                                player.sendMessage(Text.of("Each skin must be named after the minimum amount of lives you need for it"));
                                                player.sendMessage(Text.of("For example, §e\"4.png\" will activate when you have 4 or more lives"));
                                                player.sendMessage(Text.of("If you want a slim-type skin, you need to create a skins.json file in the same folder and put the following text inside:"));
                                                player.sendMessage(Text.of("§b{ \"slim\": true }"));
                                            }
                                            return 1;
                                        }
                                )
                        )
                        .then(literal("skins")
                                .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                            if (player != null) {
                                                SkinPathFinder spf = new SkinPathFinder(player.getNameForScoreboard());
                                                return spf.logSkins();
                                            }
                                            return -1;
                                        }
                                )
                        )/*
                        .then(literal("reloadAll").requires(source -> (isAdmin(source.getPlayer()) || (source.getEntity() == null)))
                                .executes(context -> {
                                            for (ServerPlayerEntity player : PlayerUtils.getAllFunctioningPlayers()){
                                                SkinPathFinder spf = new SkinPathFinder(player.getNameForScoreboard());
                                                if (spf.hasSkins()){
                                                    try {
                                                        reloadSkin(player);
                                                    } catch (NullPointerException ignored){}
                                                }
                                            }
                                            return -1;
                                        }
                                )
                        )*/
                /*
                        .then(literal("steal")
                                .requires(source -> isAdmin(source.getPlayer()))
                                .then(argument("player", EntityArgumentType.player()).executes( context -> {
                                    stealSkin(context.getSource().getPlayer(), EntityArgumentType.getPlayer(context, "player"));
                                    return 1;
                                }))
                        )*/
                /*
                        .then(literal("clearskin")
                                .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                            if (player != null) {
                                                SkinCommand.setSkin(player, () -> fetchSkinByName(player.getName().getString()));
                                            }
                                            return -1;
                                        }
                                )
                        )*/
        );
        /*
        dispatcher.register(literal("lives").executes(context -> {
            int tmp = LivesCommand.showLives(context.getSource());
            if (tmp > 0) {
                return reloadSkin(context, false);
            } else return tmp;
        }));*/ //TODO: [BUG]: /lives only runs the Life Series command, fully ignoring mine. Not sure how i can go about fixing it.
               //^ a mixin could probably overwrite it, but finding the specific return statement might be tricky
               //   ^ but do i even need that? /lifeskins reloadskin is intentionally for edge cases, one command is enough
        //dispatcher.register(literal("skin").then(literal("reload").executes(context -> reloadSkin(context))));
    }
}
