package fr.mff.facmod.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import fr.mff.facmod.core.EnumResult;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.perm.PermissionManager;

public class CommandFactionAdmin implements ICommand {


	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "fadmin";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/fadmin <user | faction | unclaim>";
	}

	@Override
	public List<String> getCommandAliases() {
		return new ArrayList<String>();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("user")) {
				if(args.length < 2) throw new WrongUsageException("/fadmin user <player>", new Object[0]);
				EnumResult result = Faction.Registry.getUserInfo(sender, args[1]);
				if(result != null) {
					sender.addChatMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			} else if(args[0].equalsIgnoreCase("faction")) {
				if(args.length < 3) throw new WrongUsageException("/fadmin faction <name> <find | delete | tp>", new Object[0]);
				if(args[2].equalsIgnoreCase("find")) {
					EnumResult result = Faction.Registry.findFaction(sender, args[1]);
					if(result != null) {
						sender.addChatMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
					}
				} else if(args[2].equalsIgnoreCase("delete")) {
					EnumResult result = Faction.Registry.destroyFaction(args[1]);
					sender.addChatMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				} else if(args[2].equalsIgnoreCase("tp")) {
					int index;
					try {
						index = Integer.valueOf(args.length >= 4 ? args[3] : "0");
					} catch (NumberFormatException e) { 
						index = 0;
					}
					EnumResult result = Faction.Registry.tpToFaction(sender, args[1], index - 1);
					if(result != null) {
						sender.addChatMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
					}
				} else throw new WrongUsageException("/fadmin faction <name> <find | delete | tp>", new Object[0]);
			} else if(args[0].equalsIgnoreCase("unclaim")) {
				String factionName = Lands.getLandFaction().remove(sender.getEntityWorld().getChunkFromBlockCoords(sender.getPosition()).getChunkCoordIntPair());
				if(factionName  != null) {
					sender.addChatMessage(new ChatComponentTranslation(EnumResult.LAND_UNCLAIMED.getLanguageKey(), EnumChatFormatting.GOLD + factionName));
				} else {
					sender.addChatMessage(new ChatComponentTranslation(EnumResult.NOT_CLAIMED_LAND.getLanguageKey(), new Object[0]));
				}
			} else {
				throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
			}
		} else {
			throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender.getCommandSenderEntity() == null ? true : PermissionManager.isOperator(sender.getName());
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
