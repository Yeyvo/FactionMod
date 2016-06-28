package fr.mff.facmod.network;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import fr.mff.facmod.FactionMod;

public class PacketRegistry {
	
	public static void init(FMLInitializationEvent event) {
		FactionMod.network.registerMessage(PacketSetFaction.Handler.class, PacketSetFaction.class, 0, Side.CLIENT);
	}

}
