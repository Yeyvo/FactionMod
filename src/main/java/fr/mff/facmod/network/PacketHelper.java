package fr.mff.facmod.network;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.core.Member;

public class PacketHelper {

	/**
	 * Try to update the client with the given {@link UUID}
	 * @param uuid
	 */
	public static void updateClientFaction(UUID uuid) {
		Entity player = MinecraftServer.getServer().getEntityFromUuid(uuid);
		if(player instanceof EntityPlayerMP) {
			String factionName = Faction.Registry.playersFactions.get(player.getUniqueID());
			PacketFaction packet = new PacketFaction(factionName == null ? "" : factionName);
			FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
		}
	}
	
	public static void updateClientRank(UUID uuid) {
		Entity player = MinecraftServer.getServer().getEntityFromUuid(uuid);
		if(player instanceof EntityPlayerMP) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			EnumRank rank = EnumRank.WITHOUT_FACTION;
			if(faction != null) {
				Member member = faction.getMember(uuid);
				if(member != null) {
					rank = member.getRank();
				}
			}
			PacketRank packet = new PacketRank(rank);
			FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
		}
	}
	
	public static void updateLandOwner(EntityPlayer player, String factionName) {
		if(factionName == null) {
			factionName = "";
		}
		PacketLandOwner packet = new PacketLandOwner(factionName);
		FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
	}
	
	public static void sendMap(EntityPlayerMP player) {
		String[] names = new String[25];
		ChunkCoordIntPair pair = player.getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
		int index = 0;
		for(int i = -2; i <= 2; i++) {
			for(int k = -2; k <= 2; k++) {
				ChunkCoordIntPair coords = player.getEntityWorld().getChunkFromChunkCoords(pair.chunkXPos + i, pair.chunkZPos + k).getChunkCoordIntPair();
				String factionName = Lands.getLandFaction().get(coords);
				if(factionName == null) {
					factionName = "";
				}
				names[index] = factionName;
				index++;
			}
		}
		PacketOpenMap packet = new PacketOpenMap(names);
		FactionMod.network.sendTo(packet, player);
	}
}
