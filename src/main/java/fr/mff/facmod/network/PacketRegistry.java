package fr.mff.facmod.network;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import fr.mff.facmod.FactionMod;

public class PacketRegistry {
	
	public static void init(FMLInitializationEvent event) {
		FactionMod.network.registerMessage(PacketFaction.Handler.class, PacketFaction.class, 0, Side.CLIENT);
		FactionMod.network.registerMessage(PacketRank.Handler.class, PacketRank.class, 1, Side.CLIENT);
		FactionMod.network.registerMessage(PacketOpenMap.Handler.class, PacketOpenMap.class, 2, Side.CLIENT);
		FactionMod.network.registerMessage(PacketLandOwner.Handler.class, PacketLandOwner.class, 4, Side.CLIENT);
	}

}
