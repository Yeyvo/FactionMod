package fr.mff.facmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**
 *	Used to instanciate and register all blocks
 */	
public class BlockRegistry {

	public static final Block homeBase = new BlockHomeBase(Material.rock).setUnlocalizedName("homeBase");

	public static void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerBlock(homeBase, "homeBase");
	}

}
