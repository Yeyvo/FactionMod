package fr.mff.facmod.handlers;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.network.PacketHelper;

public class CommonEventHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(!event.player.getEntityWorld().isRemote) {
			ChunkCoordIntPair coords = event.player.getEntityWorld().getChunkFromBlockCoords(event.player.getPosition()).getChunkCoordIntPair();
			String factionName = Lands.getLandFaction().get(coords);
			String cacheFactionName = Lands.getPlayerCache().get(event.player.getUniqueID());
			if(factionName != cacheFactionName) {
				Lands.setPlayerCache(event.player.getUniqueID(), factionName);
				if(factionName != null) {
					Faction faction = Faction.Registry.getFactionFromName(factionName);
					event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "-- " + EnumChatFormatting.GOLD + factionName + (faction == null || faction.getDescription().equals("") ? "" : EnumChatFormatting.LIGHT_PURPLE + " - " + EnumChatFormatting.BLUE + faction.getDescription()) + EnumChatFormatting.LIGHT_PURPLE + "--"));
				} else {
					event.player.addChatComponentMessage(new ChatComponentTranslation("faction.chunk.free", new Object[0]));
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PacketHelper.updateClient(event.player.getUniqueID());
	}
	
	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		if(event.player.worldObj != null && !event.player.worldObj.isRemote) {
			Lands.removePlayerCache(event.player.getUniqueID());
		}
	}

}
