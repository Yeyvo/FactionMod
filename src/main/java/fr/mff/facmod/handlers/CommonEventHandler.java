package fr.mff.facmod.handlers;

import fr.mff.facmod.core.SystemHandler;
import fr.mff.facmod.network.PacketHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CommonEventHandler {

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if(SystemHandler.getPlayers().get(event.player.getUniqueID()) == null) {
			SystemHandler.setPlayer(event.player.getUniqueID(), "");
		} else {
			PacketHelper.updateClient(event.player.getUniqueID());
		}
	}
	
}
