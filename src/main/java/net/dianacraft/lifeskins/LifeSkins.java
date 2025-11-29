package net.dianacraft.lifeskins;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.dianacraft.lifeskins.command.LifeSkinsCommand;

public class LifeSkins implements ModInitializer {
	public static final String MOD_ID = "lifeskins";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initialising Life Skins...");
		CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> {
			// Registering /skin reload
			LifeSkinsCommand.register(dispatcher);
		});
        //new ScoreboardMixin();
		//LOGGER.info("Life Skins commands initialised!");
	}
}