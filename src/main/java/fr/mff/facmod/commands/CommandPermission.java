package fr.mff.facmod.commands;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import fr.mff.facmod.core.EnumResult;
import fr.mff.facmod.perm.PermissionManager;

public class CommandPermission implements ICommand {

	public static final List<String> aliases = Lists.newArrayList();
	static {
		aliases.add("perm");
		aliases.add("p");
	}

	public static final Set<String> pArgs = Sets.newHashSet();
	static {
		pArgs.add("group");
		pArgs.add("perm");
	}

	public static final Set<String> groupArgs = Sets.newHashSet();
	static {
		groupArgs.add("create");
		groupArgs.add("remove");
	}

	public static final Set<String> groupGroup = Sets.newHashSet();
	static {
		groupGroup.add("add");
		groupGroup.add("remove");
	}

	public static final Set<String> groupAdd = Sets.newHashSet();
	static {
		groupAdd.add("perm");
		groupAdd.add("player");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "permission";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		String[] args = (String[]) pArgs.toArray();
		String s = "/" + getCommandName() + " <";
		for(int i = 0; i < args.length; i++) {
			s += args[i];
			if(i + 1 < args.length) {
				s += " | ";
			}
		}
		s += ">";
		return s;
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer && args.length >= 1) {
			EntityPlayer player = (EntityPlayer)sender;

			// Group
			if(args[0].equalsIgnoreCase("group")) {
				if(args.length < 2) throw new WrongUsageException("/permission group <create | remove | group name>", new Object[0]);

				// Group create
				if(args[1].equalsIgnoreCase("create")) {
					if(args.length < 5) throw new WrongUsageException("/permission group create <group name> <prefix> <color>", new Object[0]);
					EnumResult result = PermissionManager.createGroup(args[2], args[3], args[4]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}

				//Group remove
				else if(args[1].equalsIgnoreCase("remove")) {
					if(args.length < 3) throw new WrongUsageException("/permission group remove <group name>", new Object[0]);
					EnumResult result = PermissionManager.removeGroup(args[2]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}

				//Group <group name>
				else {
					if(args.length < 3) throw new WrongUsageException("/permission group " + args[1]  + " <add | remove>", new Object[0]);

					//Group <group name> add
					if(args[2].equalsIgnoreCase("add")) {
						if(args.length < 4) throw new WrongUsageException("/permission group " + args[1]  + " add <player | perm>", new Object[0]);

						//Group <group name> add player
						if(args[3].equalsIgnoreCase("player")) {
							if(args.length < 5) throw new WrongUsageException("/permission group " + args[1] + " add player <player name>", new Object[0]);
							EnumResult result = PermissionManager.setGroup(args[4], args[1]);
							player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
						}

						//Group <group name> add perm
						if(args[3].equalsIgnoreCase("perm")) {
							if(args.length < 5) throw new WrongUsageException("/permission group " + args[1] + " add perm <permission pattern>", new Object[0]);
							EnumResult result = PermissionManager.addPermission(args[1], args[4]);
							player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
						}
					}

					//Group <group name> remove
					if(args[2].equalsIgnoreCase("remove")) {
						if(args.length < 4) throw new WrongUsageException("/permission group " + args[1]  + " remove <player | perm>", new Object[0]);

						//TODO Change it
						//Group <group name> remove player
						if(args[3].equalsIgnoreCase("player")) {
							if(args.length < 5) throw new WrongUsageException("/permission group " + args[1] + " remove player <player name>", new Object[0]);
							EnumResult result = PermissionManager.unsetGroup(args[4]);
							player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
						}

						//Group <group name> remove perm
						if(args[3].equalsIgnoreCase("perm")) {
							if(args.length < 5) throw new WrongUsageException("/permission group " + args[1] + " remove perm <permission pattern>", new Object[0]);
							EnumResult result = PermissionManager.removePermission(args[1], args[4]);
							player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
						}
					}
				}
			}

			// Perm
			else if(args[0].equalsIgnoreCase("perm") ) {
				if(args.length < 2) throw new WrongUsageException("/permission perm <list>", new Object[0]);

				//Perm list
				if(args[1].equalsIgnoreCase("list")) {
					if(args.length < 3) throw new WrongUsageException("/permission perm list <pattern>", new Object[0]);
					EnumResult result = PermissionManager.permissionList(args[2]);
					player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
				}
			}
			
			else if(args[0].equalsIgnoreCase("user")) {
				if(args.length >= 2) {
					GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(args[1]);
					if(profile != null) {
						System.out.println(PermissionManager.getPlayerGroup(profile.getId()));
					}
				}
			}

			else {
				throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		List<String> tab = Lists.newArrayList();
		if(args.length == 1) {
			tab.addAll(getStringStartingWith(args[0], pArgs));
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("group")) {
				tab.addAll(getStringStartingWith(args[1], groupArgs));
				tab.addAll(getStringStartingWith(args[1], PermissionManager.getGroupsNames()));
			} else if(args[0].equalsIgnoreCase("perm")) {
				tab.add("list");
			}
		} else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("group")) {
				if(!args[1].equalsIgnoreCase("create")) {
					if(args[1].equalsIgnoreCase("remove")) {
						tab.addAll(getStringStartingWith(args[2], PermissionManager.getGroupsNames()));
					} else {
						tab.addAll(getStringStartingWith(args[2], groupGroup));
					}
				}
			}
		} else if(args.length == 4) {
			if(args[0].equalsIgnoreCase("group")) {
				if(!args[1].equalsIgnoreCase("create") && !args[1].equalsIgnoreCase("remove")) {
					tab.addAll(getStringStartingWith(args[3], groupAdd));
				}
			}
		} else if(args.length == 5) {
			if(args[0].equalsIgnoreCase("group")) {
				if(args[1].equalsIgnoreCase("create")) {
					for(EnumChatFormatting format : EnumChatFormatting.values()) {
						tab.add(format.getFriendlyName());
					}
				}
			}
		}
		return tab;
	}

	public List<String> getStringStartingWith(String starter, Collection<String> tests) {
		List<String> ret = Lists.newArrayList();
		for(String s : tests) {
			if(s.startsWith(starter)) {
				ret.add(s);
			}
		}
		return ret;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
