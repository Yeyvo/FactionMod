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
import net.minecraft.world.ChunkCoordIntPair;
import fr.mff.facmod.core.EnumResult;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.Lands;

public class CommandFaction extends CommandBase {

	private static final List<String> fArgs = new ArrayList<String>();
	static {
		fArgs.add("create");
		fArgs.add("join");
		fArgs.add("leave");
		fArgs.add("kick");
		fArgs.add("ban");
		fArgs.add("destroy");
		fArgs.add("claim");
		fArgs.add("open");
		fArgs.add("close");
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
							desc += args[i] + " ";
						}
					}
					EnumResult result = Faction.Registry.createFaction(player.getUniqueID(), facName, desc);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}

			// Destroy
			else if(args[0].equalsIgnoreCase("destroy")) {
				Faction.Registry.destroyFaction(player.getUniqueID());
			}

			// Join
			else if(args[0].equalsIgnoreCase("join") && args.length >= 2) {
				EnumResult result = Faction.Registry.joinFaction(player.getUniqueID(), args[1]);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Leave
			else if(args[0].equalsIgnoreCase("leave")) {
				EnumResult result = Faction.Registry.leaveFaction(player.getUniqueID());
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}
			
			// Invite
			else if(args[0].equalsIgnoreCase("invite") && args.length >= 2) {
				EnumResult result = Faction.Registry.invite(player.getUniqueID(), args[1]);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Kick
			else if(args[0].equalsIgnoreCase("kick")) {
				if(args.length >= 2) {
					EnumResult result = Faction.Registry.kickPlayer(player.getUniqueID(), args[1]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}

			// Ban
			else if(args[0].equalsIgnoreCase("ban")) {
				if(args.length >= 2) {
					EnumResult result = Faction.Registry.banPlayer(player.getUniqueID(), args[1]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}

			// Open
			else if(args[0].equalsIgnoreCase("open")) {
				EnumResult result = Faction.Registry.setFactionOpen(player.getUniqueID(), true);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Close
			else if(args[0].equalsIgnoreCase("clo")) {
				EnumResult result = Faction.Registry.setFactionOpen(player.getUniqueID(), false);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Claim
			else if(args[0].equalsIgnoreCase("claim")) {
				ChunkCoordIntPair pair = player.getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
				EnumResult result = Lands.claimChunk(player.getUniqueID(), pair);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Un-Claim
			else if(args[0].equalsIgnoreCase("unclaim")) {
				ChunkCoordIntPair pair = player.getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
				EnumResult result = Lands.unClaimChunk(player.getUniqueID(), pair);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Description
			else if(args[0].equalsIgnoreCase("description") || args[0].equalsIgnoreCase("desc")) {
				String desc = "";
				if(args.length > 1) {
					for(int i = 1; i < args.length; i++) {
						desc += args[i] + " ";
					}
				}
				EnumResult result = Faction.Registry.changeDescription(player.getUniqueID(), desc);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}
			//Mosca421 === /sethome
			
			else if(args[0].equalsIgnoreCase("sethome")) {

			}
			else if(args[0].equalsIgnoreCase("home")) {

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
