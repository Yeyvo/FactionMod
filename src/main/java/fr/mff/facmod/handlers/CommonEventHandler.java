package fr.mff.facmod.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.network.PacketHelper;

public class CommonEventHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Lands.onPlayerTick(event);
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PacketHelper.updateClient(event.player.getUniqueID());
	}
	
	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		Lands.onPlayerLeave(event);
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Homes.onPlayerRespawn(event);
	}

}
