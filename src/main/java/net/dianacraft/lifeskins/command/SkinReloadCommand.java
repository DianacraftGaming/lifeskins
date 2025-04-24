package net.dianacraft.lifeskins.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import org.samo_lego.fabrictailor.command.SkinCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.samo_lego.fabrictailor.util.SkinFetcher.setSkinFromFile;
import static net.minecraft.server.command.CommandManager.*;

public class SkinReloadCommand {
    public static final String MOD_ID = "lifeskins";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("skin")
            .then(literal("reload")
                .executes(context -> {
                    //context.getSource().sendFeedback(() -> Text.literal("Reloading the skin..."), false);
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    String skinFilePath = "config/lifeskins/Player/2.png";
                    SkinCommand.setSkin(player, () -> setSkinFromFile(skinFilePath, false));

                    LOGGER.info("supposedly ran the command");
                    //TODO: /skin set upload classic config/lifeskins/Player/2.png
                    return 1;
                    }
                )
            )
        );
    }
}

// CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//	var redirectedBy = dispatcher.register(CommandManager.literal("redirected_by").executes(FabricDocsReferenceCommands::executeRedirectedBy));
//	dispatcher.register(CommandManager.literal("to_redirect").executes(FabricDocsReferenceCommands::executeRedirectedBy).redirect(redirectedBy));
//});
