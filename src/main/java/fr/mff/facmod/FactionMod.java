package fr.mff.facmod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.apache.logging.log4j.Logger;

import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.core.FactionHelper;
import fr.mff.facmod.core.SystemHandler;
import fr.mff.facmod.core.extendedProperties.ExtendedPropertieFaction;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.handlers.GuiHandler;
import fr.mff.facmod.items.ItemRegistry;
import fr.mff.facmod.proxy.CommonProxy;
import fr.mff.facmod.recipes.RecipeRegistry;
import fr.mff.facmod.tileentities.TileEntityRegistry;
import fr.mff.facmod.tileentities.network.PacketRegistry;

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

	public static SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit(event);
		BlockRegistry.preInit(event);
		ItemRegistry.preInit(event);

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
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		/**
		 * For tests
		 */
		event.registerServerCommand(new ICommand() {

			@Override
			public int compareTo(ICommand arg0) {
				return 0;
			}

			@Override
			public void processCommand(ICommandSender sender, String[] args) throws CommandException {
				if(sender instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer)sender;
					if(args.length >= 1) {
						if(args[0].equalsIgnoreCase("create") && args.length >= 2) {
							String desc = "";
							if(args.length >= 3) {
								desc = args[2];
							}
							String facName = args[1];
							FactionHelper.createFaction(player, facName, desc);
						}

						if(args[0].equalsIgnoreCase("leave")) {
							Faction faction = FactionHelper.getPlayerFaction(player);
							if(faction != null) {
								faction.removePlayer(player);
							}
							if(args.length >= 2) {
								if(args[1].equalsIgnoreCase("force")) {
									ExtendedPropertieFaction prop = ExtendedPropertieFaction.get(player);
									prop.setNoFaction();
								}
							}
						}
					}
				}
			}

			@Override
			public boolean isUsernameIndex(String[] args, int index) {
				return false;
			}

			@Override
			public String getCommandUsage(ICommandSender sender) {
				return "";
			}

			@Override
			public String getCommandName() {
				return "f";
			}

			@Override
			public List<String> getCommandAliases() {
				return new ArrayList<String>();
			}

			@Override
			public boolean canCommandSenderUseCommand(ICommandSender sender) {
				return true;
			}

			@Override
			public List<String> addTabCompletionOptions(ICommandSender sender,
					String[] args, BlockPos pos) {
				return new ArrayList<String>();
			}
		});
	}

}
