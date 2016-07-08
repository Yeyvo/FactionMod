package fr.mff.facmod;

import org.apache.logging.log4j.Logger;

import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.commands.CommandRegistry;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.core.FactionSaver;
import fr.mff.facmod.entity.EntityDynamite;
import fr.mff.facmod.handlers.GuiHandler;
import fr.mff.facmod.items.ItemRegistry;
import fr.mff.facmod.network.PacketRegistry;
import fr.mff.facmod.network.PacketWandActivate;
import fr.mff.facmod.proxy.CommonProxy;
import fr.mff.facmod.recipes.RecipeRegistry;
import fr.mff.facmod.tileentities.TileEntityRegistry;
import fr.mff.facmod.util.conversion.CustomMappingManager;
import fr.mff.facmod.util.conversion.StackedBlockManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author BrokenSwing
 * Version : versionMod.release.bugFix
 */
@Mod(modid = FactionMod.MODID, version="0.0.1", guiFactory="fr.mff.facmod.client.gui.FactionGuiFactory")
public class FactionMod {

	public static final String MODID = "faction";
	public static int IDE;
	@Instance
	public static FactionMod INSTANCE;

	@SidedProxy(clientSide = "fr.mff.facmod.proxy.ClientProxy", serverSide = "fr.mff.facmod.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static Logger logger;
	public StackedBlockManager blockCache;
	public CustomMappingManager mappingManager;
	public SimpleNetworkWrapper networkWrapper;
	public static CreativeTabs factionTabs = new CreativeTabs("FactionTab") {
		
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(BlockRegistry.homeBase);
		}
	};
	
	public static SimpleNetworkWrapper network;	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("factionmod");
		this.networkWrapper.registerMessage(PacketWandActivate.Handler.class, PacketWandActivate.class, 0, Side.SERVER);
		logger = event.getModLog();
		ConfigFaction.preInit(event);
		BlockRegistry.preInit(event);
		ItemRegistry.preInit(event);
		proxy.preInit(event);
	    this.blockCache = new StackedBlockManager();
	    this.mappingManager = new CustomMappingManager();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);

		RecipeRegistry.init(event);
		TileEntityRegistry.init(event);

		NetworkRegistry.INSTANCE.registerGuiHandler(FactionMod.INSTANCE, new GuiHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel(FactionMod.MODID);
		PacketRegistry.init(event);
	    Render(EntityDynamite.class, "entitydynamite", 65, this, 512, 1, true);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		FactionSaver.onServerStarting(event);
		CommandRegistry.onServerStarting(event);
	}
	  public void Set(Class<? extends Entity> par1, String par2, int par3)
	  {
	    EntityRegistry.registerGlobalEntityID(par1, par2, par3);
	  }
	  public void Render(Class<? extends Entity> par1, String par2, int par3, Object par4, int par5, int par6, boolean par7)
	  {
	    EntityRegistry.registerModEntity(par1, par2, par3, par4, par5, par6, par7);
	  }

}
