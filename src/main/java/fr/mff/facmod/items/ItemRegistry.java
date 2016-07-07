package fr.mff.facmod.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *	Used to instanciate and register all items
 */
public class ItemRegistry {
	
	public static final Item landMap = new ItemLandMap().setUnlocalizedName("landMap");
	
	public static void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerItem(landMap, "landMap");
	}

}
