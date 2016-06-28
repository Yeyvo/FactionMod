package fr.mff.facmod.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandRegistry {

	public static void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFaction());
	}

}
