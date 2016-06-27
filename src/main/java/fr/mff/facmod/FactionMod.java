package fr.mff.facmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import org.apache.logging.log4j.Logger;

import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.handlers.GuiHandler;
import fr.mff.facmod.items.ItemRegistry;
import fr.mff.facmod.proxy.CommonProxy;
import fr.mff.facmod.recipes.RecipeRegistry;
import fr.mff.facmod.tileentities.TileEntityRegistry;

/**
 * @author BrokenSwing
 * Version : versionMod.release.bugFix
 */
@Mod(modid = FactionMod.MODID, version="0.0.1")
public class FactionMod {
	
	public static final String MODID = "faction";
	
	@Instance
	public static FactionMod INSTANCE;
	
	@SidedProxy(clientSide = "fr.mff.facmod.proxy.ClientProxy", serverSide = "fr.mff.facmod.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit(event);
		BlockRegistry.preInit(event);
		ItemRegistry.preInit(event);
		//ceci est un test test test
		//Huston ça marche ?

	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		
		RecipeRegistry.init(event);
		TileEntityRegistry.init(event);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(FactionMod.INSTANCE, new GuiHandler());
	}

}
