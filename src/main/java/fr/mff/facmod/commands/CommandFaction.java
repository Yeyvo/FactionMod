package fr.mff.facmod.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import fr.mff.facmod.core.FactionHelper;
import fr.mff.facmod.core.features.Faction;

public class CommandFaction extends CommandBase {

	private static final List<String> fArgs = new ArrayList<String>();
	static {
		fArgs.add("create");
		fArgs.add("join");
		fArgs.add("leave");
		fArgs.add("kick");
		fArgs.add("ban");
		fArgs.add("destroy");
	}

	private static final List<String> commandAliases = new ArrayList<String>();
	static {
		commandAliases.add("fac");
		commandAliases.add("f");
	}

	@Override
	public int compareTo(ICommand o) {
		return 1;
	}

	@Override
	public String getCommandName() {
		return "faction";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		String usage = "/faction <";
		for(int i = 0 ; i < fArgs.size(); i++) {
			usage += fArgs.get(i);
			if(i < fArgs.size() - 1) {
				usage += " | ";
			}
		}
		usage += ">";
		return usage;
	}

	@Override
	public List<String> getCommandAliases() {
		return commandAliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		//TODO End the handling
		if(args.length < 1) {
			throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
		} else {
			EntityPlayer player = getCommandSenderAsPlayer(sender);

			// Create
			if(args[0].equalsIgnoreCase("create")) {
				if(args.length <= 1) {
					throw new WrongUsageException("/faction create <factionName> [desc] ", new Object[0]);
				} else {
					String facName = args[1];
					String desc = "";
					if(args.length >= 3) {
						for(int i = 2; i < args.length; i++) {
							desc += args[i];
						}
					}
					if(FactionHelper.tryCreateFaction(player.getUniqueID(), facName, desc)) {
						player.addChatComponentMessage(new ChatComponentTranslation("command.faction.create.success", EnumChatFormatting.YELLOW + facName));
					} else {
						player.addChatComponentMessage(new ChatComponentTranslation("command.faction.create.fail", EnumChatFormatting.YELLOW + facName));
					}
				}
			}

			// Join
			else if(args[0].equalsIgnoreCase("join") && args.length >= 2) {

			}

			// Leave
			else if(args[0].equalsIgnoreCase("leave")) {
				Faction faction = FactionHelper.getPlayerFaction(player.getUniqueID());
				if(faction != null) {
					faction.removePlayer(player.getUniqueID());
					player.addChatMessage(new ChatComponentTranslation("command.faction.leave.success", new Object[]{EnumChatFormatting.YELLOW + faction.getName()}));
				} else {
					player.addChatComponentMessage(new ChatComponentTranslation("command.faction.leave.fail", new Object[0]));
				}
			}

			else {
				throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
			}
		}
	}


	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length == 1) {
			return fArgs;
		}
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		if(args.length == 1 && index == 1) {
			if(args[0].equalsIgnoreCase("invite")) {
				return true;
			}
			if(args[0].equalsIgnoreCase("ban")) {
				return true;
			}
		}
		return false;
	}

}
