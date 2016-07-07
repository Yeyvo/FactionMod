package fr.mff.facmod.handlers;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
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
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		Homes.onPlayerTick(event);
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Homes.onLivingHurt(event);
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
	public void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		Lands.onPlayerBreakBlock(event);
	}
}
