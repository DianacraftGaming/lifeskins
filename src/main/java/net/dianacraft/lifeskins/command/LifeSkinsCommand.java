package net.dianacraft.lifeskins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dianacraft.lifeskins.util.SkinPathFinder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.samo_lego.fabrictailor.command.SkinCommand;

import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.minecraft.server.command.CommandManager.literal;
import static org.samo_lego.fabrictailor.util.SkinFetcher.setSkinFromFile;

public class LifeSkinsCommand {

    public static int reloadSkin(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //context.getSource().sendFeedback(() -> Text.literal("Reloading the skin..."), false);
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        SkinPathFinder spf = new SkinPathFinder(player);
        if (!currentSeason.hasAssignedLives(player)) {
            player.sendMessage(Text.of("You have not been assigned any lives yet."));
            return -1;
        }

        if (spf.hasSkins()) {
            //SkinCommand.setSkin(player, () -> setSkinFromFile("config/lifeskins/Player/1.png", true));
            SkinCommand.setSkin(player, () -> setSkinFromFile(spf.getSkinPath(), SkinPathFinder.getSlim(spf.getSkin(currentSeason.getPlayerLives(player)))));  // TODO: [BUG] skins always set as classic, even if i hardcode true, seems to be a fabrictailor issue
        } else {
            player.sendMessage(Text.of("§cCouldn't find any life skins! Make sure you set them up correctly, run \"/lifeskins info\" to get setup instructions"), false);
            return -1;
        }
        return 1;
    }

    public static void reloadSkin(ServerPlayerEntity player, int lives) throws CommandSyntaxException {
        SkinPathFinder spf = new SkinPathFinder(player);
        if (spf.hasSkins()) {
            SkinCommand.setSkin(player, () -> setSkinFromFile(spf.getSkinPath(lives), SkinPathFinder.getSlim(spf.getSkin(currentSeason.getPlayerLives(player)))));  // TODO: [BUG] skins always set as classic, even if i hardcode true, seems to be a fabrictailor issue
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("lifeskins")
                        .then(literal("reloadskin")
                                .executes(LifeSkinsCommand::reloadSkin)
                        )
                        .then(literal("info")
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
                                                SkinPathFinder spf = new SkinPathFinder(player);
                                                return spf.logSkins();
                                            }
                                            return -1;
                                        }
                                )
                        )/*
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
