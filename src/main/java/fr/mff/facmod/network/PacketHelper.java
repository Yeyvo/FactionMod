package fr.mff.facmod.network;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.SystemHandler;

public class PacketHelper {

	public static void updateClient(UUID uuid) {
		Entity player = MinecraftServer.getServer().getEntityFromUuid(uuid);
		if(player instanceof EntityPlayerMP) {
			String factionName = SystemHandler.getPlayers().get(player.getUniqueID());
			PacketSetFaction packet = new PacketSetFaction(factionName);
			FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
		}
	}

}
