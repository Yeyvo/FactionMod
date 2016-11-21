package fr.mff.facmod.commands;

import java.util.List;

import fr.mff.facmod.core.Homes;
import fr.mff.facmod.perm.PermissionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class CommandEnderAdmin extends CommandBase {
	@Override
	public String getCommandName() {
		return "ender";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/ender <player>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
	
		if(args.length <= 0){
			throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
		}
			EntityPlayerMP playermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
			    InventoryEnderChest inventoryenderchest = playermp.getInventoryEnderChest();
			    if (inventoryenderchest != null)
			    {
			    	playermp.displayGUIChest(inventoryenderchest);

			    }
			    else {
					sender.addChatMessage(new ChatComponentTranslation("InventoryEnderChest is null !!", EnumChatFormatting.RED));
				}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender.getCommandSenderEntity() == null ? true : PermissionManager.isOperator(sender.getName());
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames())
				: null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return true;
	}
}