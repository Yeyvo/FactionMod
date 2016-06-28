package fr.mff.facmod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.SystemHandler;

public class PacketHelper {

	public static void updateClient(EntityPlayer player) {
		if(player instanceof EntityPlayerMP) {
			String factionName = SystemHandler.getPlayers().get(player.getUniqueID());
			PacketSetFaction packet = new PacketSetFaction(factionName);
			FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
		}
	}

}
