package fr.mff.facmod.blocks;

import fr.mff.facmod.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**
 *	Used to instanciate and register all blocks
 */	


public class BlockRegistry {
	
	public static Block FactionHome;
	public static CommonProxy proxy;
	
	public static void preInit(FMLPreInitializationEvent event) {
		
		FactionHome = new FactionHome(Material.rock);
	}
	public static void init(FMLInitializationEvent event) {
		
		GameRegistry.registerBlock(FactionHome, "FactionHome");
	}

}
