package fr.mff.facmod.tileentities;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Used to registry TileEntities with GameRegistry
 */
public class TileEntityRegistry {
	
	public static void init(FMLInitializationEvent event) {
	 GameRegistry.registerTileEntity(TileEntityHomeBase.class, "tileentityhomebase");
	 GameRegistry.registerTileEntity(TileEntityBlockXray.class, "tileentityblockxray");

	}

}
