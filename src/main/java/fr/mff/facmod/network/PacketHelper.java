package fr.mff.facmod.network;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.SystemHandler;

public class PacketHelper {

	/**
	 * Try to update the client with the given {@link UUID}
	 * @param uuid
	 */
	public static void updateClient(UUID uuid) {
		Entity player = MinecraftServer.getServer().getEntityFromUuid(uuid);
		if(player instanceof EntityPlayerMP) {
			String factionName = SystemHandler.getPlayers().get(player.getUniqueID());
			PacketFaction packet = new PacketFaction(factionName);
			FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
		}
	}

}
