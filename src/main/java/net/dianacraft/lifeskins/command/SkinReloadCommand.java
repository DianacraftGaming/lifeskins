package net.dianacraft.lifeskins.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import static net.minecraft.server.command.CommandManager.*;
public class SkinReloadCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("skin")
            .then(literal("reload")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("Reloading the skin..."), false);
                    //TODO: Figure out how the skin paths work
                    //TODO: Possibly downgrade fabrictailor, as it crashes when i open the menu
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
