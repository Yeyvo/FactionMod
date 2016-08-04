package fr.mff.facmod.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.ChunkCoordIntPair;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.core.EnumResult;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.network.PacketHelper;

public class CommandFaction extends CommandBase {

	private static final List<String> fArgs = new ArrayList<String>();
	static {
		fArgs.add("create");
		fArgs.add("destroy");
		fArgs.add("join");
		fArgs.add("leave");
		fArgs.add("promote");
		fArgs.add("invite");
		fArgs.add("sethome");
		fArgs.add("home");
		fArgs.add("kick");
		fArgs.add("ban");
		fArgs.add("unban");
		fArgs.add("open");
		fArgs.add("close");
		fArgs.add("claim");
		fArgs.add("unclaim");
		fArgs.add("description");
		fArgs.add("info");
		fArgs.add("map");
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
		if(args.length < 1) {
			throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
		} else {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);

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
				EnumResult result = Faction.Registry.destroyFaction(player.getUniqueID());
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Join
			else if(args[0].equalsIgnoreCase("join")) {
				if(args.length >= 2) {
				EnumResult result = Faction.Registry.joinFaction(player.getUniqueID(), args[1]);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				} else {
					throw new WrongUsageException("/faction join <faction>", new Object[0]);
				}
			}

			// Leave
			else if(args[0].equalsIgnoreCase("leave")) {
				EnumResult result = Faction.Registry.leaveFaction(player.getUniqueID());
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Invite
			else if(args[0].equalsIgnoreCase("invite")) {
				if(args.length >= 2) {
					EnumResult result = Faction.Registry.invite(player.getUniqueID(), args[1]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				} else {
					throw new WrongUsageException("/faction invite <player>", new Object[0]);
				}
			}

			// SetHome
			else if(args[0].equalsIgnoreCase("sethome")) {
				EnumResult result = Homes.setHome(player.getUniqueID(), player.getPosition());
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Home
			else if(args[0].equalsIgnoreCase("home")) {
				EnumResult result = Homes.goToHome(player);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Kick
			else if(args[0].equalsIgnoreCase("kick")) {
				if(args.length >= 2) {
					EnumResult result = Faction.Registry.kickPlayer(player.getUniqueID(), args[1]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				} else {
					throw new WrongUsageException("/faction kick <player>", new Object[0]);
				}
			}

			// Ban
			else if(args[0].equalsIgnoreCase("ban")) {
				if(args.length >= 2) {
					EnumResult result = Faction.Registry.banPlayer(player.getUniqueID(), args[1]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				} else {
					throw new WrongUsageException("/faction ban <player>", new Object[0]);
				}
			}
			
			// Unban
			else if(args[0].equalsIgnoreCase("unban")) {
				if(args.length >= 2) {
					EnumResult result = Faction.Registry.unbanPlayer(player.getUniqueID(), args[1]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}

			// Promote
			else if(args[0].equalsIgnoreCase("promote")) {
				if(args.length >= 3) {
					EnumResult result = Faction.Registry.promote(player.getUniqueID(), args);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				} else {
					throw new WrongUsageException("/faction promote <player> <rank>", new Object[0]);
				}
			}

			// Open
			else if(args[0].equalsIgnoreCase("open")) {
				EnumResult result = Faction.Registry.setFactionOpen(player.getUniqueID(), true);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Close
			else if(args[0].equalsIgnoreCase("close")) {
				EnumResult result = Faction.Registry.setFactionOpen(player.getUniqueID(), false);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Claim
			else if(args[0].equalsIgnoreCase("claim")) {
				String claimerName = args.length > 1 ? args[1] : player.getName();
				EnumResult result = Lands.claimChunk(player.getUniqueID(), claimerName);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			// Un-Claim
			else if(args[0].equalsIgnoreCase("unclaim")) {
				ChunkCoordIntPair pair = player.getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
				EnumResult result = Lands.unClaimChunk(player, pair);
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

			// Info
			else if(args[0].equalsIgnoreCase("info")) {
				EnumResult result = Faction.Registry.info(player, args);
				if(result != null) {
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}

			// Members
			else if(args[0].equalsIgnoreCase("members")) {
				EnumResult result = Faction.Registry.members(player, args);
				if(result != null) {
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}
			
			// Map
			else if(args[0].equalsIgnoreCase("map")) {
				PacketHelper.sendMap(player);
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
			List<String> completements = new ArrayList<String>();
			for(String str : fArgs) {
				if(str.startsWith(args[0])) {
					completements.add(str);
				}
			}
			return completements;
		} else if(args.length == 3) {
			List<String> completements = new ArrayList<String>();
			if(args[0].equalsIgnoreCase("promote")) {
				for(EnumRank rank : EnumRank.values()) {
					if(rank.name().toLowerCase().startsWith(args[2].toLowerCase())) {
						if(rank != EnumRank.WITHOUT_FACTION) {
							completements.add(rank.name());
						}
					}
				}
				return completements;
			}
		}
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
