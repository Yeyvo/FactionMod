package fr.mff.facmod.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import fr.mff.facmod.config.ConfigFaction;

public class CommandRegistry {

	public static void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFaction());
		event.registerServerCommand(new CommandZone());
		if(ConfigFaction.ENABLE_PERMISSION) {
			event.registerServerCommand(new CommandPermission());
		}
		event.registerServerCommand(new CommandFactionAdmin());
	}

}
