package fr.mff.facmod.network;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.core.Faction;
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

}
