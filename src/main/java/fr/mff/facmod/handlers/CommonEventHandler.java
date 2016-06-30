package fr.mff.facmod.handlers;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.facmod.core.FactionHelper;
import fr.mff.facmod.core.SystemHandler;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.network.PacketHelper;

public class CommonEventHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(!event.player.getEntityWorld().isRemote) {
			ChunkCoordIntPair coords = event.player.getEntityWorld().getChunkFromBlockCoords(event.player.getPosition()).getChunkCoordIntPair();
			String factionName = SystemHandler.getLands().get(coords);
			String cacheFactionName = SystemHandler.getPlayerLandCache().get(event.player.getUniqueID());
			if(factionName != cacheFactionName) {
				SystemHandler.getPlayerLandCache().put(event.player.getUniqueID(), factionName);
				if(factionName != null) {
					Faction faction = FactionHelper.getFactionFromName(factionName);
					event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW + factionName + (faction == null || faction.getDescription().equals("") ? "" : EnumChatFormatting.RESET + " - " + EnumChatFormatting.AQUA + faction.getDescription())));
				} else {
					event.player.addChatComponentMessage(new ChatComponentTranslation("faction.chunk.free", new Object[0]));
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if(SystemHandler.getPlayers().get(event.player.getUniqueID()) == null) {
			SystemHandler.setPlayer(event.player.getUniqueID(), "");
		} else {
			PacketHelper.updateClient(event.player.getUniqueID());
		}
	}

}
