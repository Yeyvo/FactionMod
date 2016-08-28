package fr.mff.facmod.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import fr.mff.facmod.perm.PermissionManager;

public class CommandClearItems implements ICommand {
	
	private static final ArrayList<String> aliases = new ArrayList<String>();
	{
		aliases.add("ci");
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "clearitems";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/clearitems";
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		for(WorldServer w : MinecraftServer.getServer().worldServers) {
			for(Entity e : w.loadedEntityList) {
				if(e instanceof EntityItem) {
					e.setDead();
				}
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender.getCommandSenderEntity() == null ? true : PermissionManager.isOperator(sender.getCommandSenderEntity().getName());
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
