package net.dianacraft.lifeskins.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
//import org.samo_lego.fabrictailor.command.SkinCommand;
//import org.samo_lego.fabrictailor.util.SkinFetcher;

import static net.minecraft.server.command.CommandManager.*;
//import static org.samo_lego.fabrictailor.util.SkinFetcher.setSkinFromFile;

public class SkinReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("skin")
            .then(literal("reload")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("Reloading the skin..."), false);
                    return 1;}
                )
            )
        );
    }
}

// CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//	var redirectedBy = dispatcher.register(CommandManager.literal("redirected_by").executes(FabricDocsReferenceCommands::executeRedirectedBy));
//	dispatcher.register(CommandManager.literal("to_redirect").executes(FabricDocsReferenceCommands::executeRedirectedBy).redirect(redirectedBy));
//});
