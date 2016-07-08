package fr.mff.facmod.items;

import fr.mff.facmod.wand.util.RestrictedWand;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *	Used to instanciate and register all items
 */
public class ItemRegistry {
	
	public static final Item chestWatcher = new ItemChestWatcher().setUnlocalizedName("chestWatcher"); 
	public static final Item landMap = new ItemLandMap().setUnlocalizedName("landMap");
	public static final Item homeFinder = new ItemHomeFinder().setUnlocalizedName("homeFinder");
	public static final Item dynamite = new ItemDynamite().setUnlocalizedName("dynamite");
	public static final Item briseObsi = new ItemBriseObsi().setUnlocalizedName("briseObsi");
	public static final Item builderWand = new ItemBuilderWand(new RestrictedWand(9)).setUnlocalizedName("builderWand");

	public static void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerItem(landMap, "landMap");
		GameRegistry.registerItem(chestWatcher, "chestWatcher");
		GameRegistry.registerItem(homeFinder, "homeFinder");
	    GameRegistry.registerItem(dynamite, "dynamite");
	    GameRegistry.registerItem(briseObsi, "briseObsi");
	    GameRegistry.registerItem(builderWand, "builderWand");



	}

}
