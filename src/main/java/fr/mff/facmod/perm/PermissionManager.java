package fr.mff.facmod.perm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.core.EnumResult;


public class PermissionManager extends WorldSavedData {

	public static Set<Group> groups = Sets.newHashSet();
	public static Group defaultGroup = null;
	public static HashMap<UUID, Group> players = Maps.newHashMap();
	public static PermissionBase permissions = new PermissionBase("", null);
	public static HashMap<UUID, Set<String>> playersSpecialPermissions = Maps.newHashMap();
	public static HashMap<UUID, Set<String>> playersSpecialRestrictions = Maps.newHashMap();
	static {
		permissions.add("world.block.break");
		permissions.add("world.block.place");
		permissions.add("world.block.interact");
	}

	private static PermissionManager INSTANCE = null;

	public PermissionManager(String name) {
		super(name);
		INSTANCE = this;
	}

	public static void save() {
		if(INSTANCE != null) {
			INSTANCE.markDirty();
		}
	}

	public static EnumResult addPlayerPermission(String playerName, String pattern) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(playerName);
		if(profile != null) {
			if(pattern.matches("^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)|(([a-zA-Z0-9]+\\.)*\\*)$")) {
				Set<String> perms = getPaths(permissions.get(pattern));
				Set<String> currentsPermsAdded = playersSpecialPermissions.get(profile.getId());
				Set<String> currentsPermsRemoved = playersSpecialRestrictions.get(profile.getId());
				if(currentsPermsAdded == null) {
					currentsPermsAdded = Sets.newHashSet();
					playersSpecialPermissions.put(profile.getId(), currentsPermsAdded);
				}
				if(currentsPermsRemoved != null) {
					currentsPermsRemoved.removeAll(perms);
				}
				currentsPermsAdded.addAll(perms);

				save();
				return EnumResult.SPECIAL_PERMISSION_ADDED.clear().addInformation(pattern).addInformation(profile.getName());
			}
			return EnumResult.WRONG_SYNTAX;
		}
		return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(playerName);
	}
	
	public static EnumResult addPlayerRestriction(String playerName, String pattern) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(playerName);
		if(profile != null) {
			if(pattern.matches("^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)|(([a-zA-Z0-9]+\\.)*\\*)$")) {
				Set<String> perms = getPaths(permissions.get(pattern));
				Set<String> currentsPermsAdded = playersSpecialPermissions.get(profile.getId());
				Set<String> currentsPermsRemoved = playersSpecialRestrictions.get(profile.getId());
				if(currentsPermsRemoved == null) {
					currentsPermsRemoved = Sets.newHashSet();
					playersSpecialRestrictions.put(profile.getId(), currentsPermsRemoved);
				}
				if(currentsPermsAdded != null) {
					currentsPermsAdded.removeAll(perms);
				}
				currentsPermsRemoved.addAll(perms);

				save();
				return EnumResult.SPECIAL_RESTRICTION_ADDED.clear().addInformation(pattern).addInformation(profile.getName());
			}
			return EnumResult.WRONG_SYNTAX;
		}
		return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(playerName);
	}

	public static EnumResult setGroup(String playerName, String groupName) {
		Group g = getGroupFromName(groupName);
		if(g != null) {
			GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(playerName);
			if(profile != null) {
				Group playerGroup = getPlayerGroup(profile.getId());
				if(playerGroup == null) {
					g.addMember(profile.getId());
					players.put(profile.getId(), g);
					Entity entity = MinecraftServer.getServer().getEntityFromUuid(profile.getId());
					if(entity instanceof EntityPlayer) {
						((EntityPlayer)entity).refreshDisplayName();
					}
					save();
					return EnumResult.PLAYER_ADDED_TO_GROUP.clear().addInformation(profile.getName()).addInformation(g.getName());
				}
				return EnumResult.PLAYER_ALREADY_IN_THE_GROUP.clear().addInformation(profile.getName()).addInformation(playerGroup.getName());
			}
			return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(playerName);
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(groupName);
	}

	public static EnumResult unsetGroup(String playerName) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(playerName);
		if(profile != null) {
			Group playerGroup = getPlayerGroup(profile.getId());
			if(playerGroup != null) {
				playerGroup.removeMember(profile.getId());
				players.remove(profile.getId());
				Entity entity = MinecraftServer.getServer().getEntityFromUuid(profile.getId());
				if(entity instanceof EntityPlayer) {
					((EntityPlayer)entity).refreshDisplayName();
				}
				save();
				return EnumResult.PLAYER_REMOVED_FROM_GROUP.clear().addInformation(profile.getName()).addInformation(playerGroup.getName());
			}
			return EnumResult.PLAYER_NOT_IN_A_GROUP.clear().addInformation(profile.getName());
		}
		return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(playerName);
	}

	public static EnumResult createGroup(String name, String prefix, String color) {
		if(!name.equalsIgnoreCase("create") && 
				!name.equalsIgnoreCase("remove") && 
				!name.equalsIgnoreCase("operator") &&
				!name.equalsIgnoreCase("default")) {
			Group group = new Group(name, prefix, EnumChatFormatting.getValueByName(color));
			if(groups.add(group)) {
				save();
				return EnumResult.GROUP_CREATED.clear().addInformation(group.getName());
			}
			return EnumResult.EXISTING_GROUP.clear().addInformation(name);
		}
		return EnumResult.INVALID_NAME.clear().addInformation(name);
	}

	public static EnumResult removeGroup(String name) {
		Group toRemove = null;
		for(Group g : groups) {
			if(g.getName().equalsIgnoreCase(name)) {
				toRemove = g;
				break;
			}
		}
		if(toRemove != null) {
			groups.remove(toRemove);
			for(UUID uuid : toRemove.getMembers()) {
				players.remove(uuid);
				Entity entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
				if(entity instanceof EntityPlayer) {
					((EntityPlayer)entity).refreshDisplayName();
				}
			}
			save();
			return EnumResult.GROUP_REMOVED.clear().addInformation(toRemove.getName());
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(name);
	}

	public static EnumResult addPermission(String groupName, String perm) {
		Group g = getGroupFromName(groupName);
		if(g != null) {
			if(perm.matches("^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)|(([a-zA-Z0-9]+\\.)*\\*)$")) {
				Set<PermissionBase> perms = permissions.get(perm);
				for(PermissionBase base : perms) {
					g.addPermission(base.getPath());
				}
				save();
				return EnumResult.PERMISSION_ADDED.clear().addInformation(perm).addInformation(g.getName());
			}
			return EnumResult.WRONG_SYNTAX;
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(groupName);
	}

	public static EnumResult removePermission(String groupName, String perm) {
		Group g = getGroupFromName(groupName);
		if(g != null) {
			if(perm.matches("^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)|(([a-zA-Z0-9]+\\.)*\\*)$")) {
				Set<PermissionBase> perms = permissions.get(perm);
				for(PermissionBase base : perms) {
					g.removePermission(base.getPath());
				}
				save();
				return EnumResult.PERMISSION_REMOVED.clear().addInformation(perm).addInformation(g.getName());
			}
			return EnumResult.WRONG_SYNTAX;
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(groupName);
	}

	public static Set<PermissionBase> getPermission(String name) {
		return permissions.get(name);
	}

	public static Group getPlayerGroup(UUID uuid) {
		return players.get(uuid);
	}

	public static Group getGroupFromName(String name) {
		for(Group g : groups) {
			if(g.getName().equalsIgnoreCase(name)) {
				return g;
			}
		}
		return null;
	}

	public static boolean canEntityExecuteCommand(Entity entity, ICommand command) {

		/*Group g = players.get(entity.getUniqueID());
		if(g != null) {
			if(g.hasPermission("command." + command.getCommandName())) {
				return true;
			}
			return false;
		} else {
			if(isOperator(entity.getName())) {
				return true;
			}
		}*/
		return hasEntityPermission(entity, "command." + command.getCommandName());
	}

	public static boolean isOperator(String name) {
		if(name.equalsIgnoreCase(MinecraftServer.getServer().getServerOwner())) {
			return true;
		}
		for(String s : MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames()) {
			if(s.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOperator(UUID uuid) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getProfileByUUID(uuid);
		if(profile != null) {
			return isOperator(profile.getName());
		}
		return false;
	}
	
	public static EnumResult setDefaultGroup(String groupName) {
		Group g = getGroupFromName(groupName);
		if(g != null) {
			if(defaultGroup != null) {
				defaultGroup.setDefaultGroup(false);
			}
			g.setDefaultGroup(true);
			defaultGroup = g;
			save();
			return EnumResult.DEFAULT_GROUP_CHANGED.clear().addInformation(g.getName());
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(groupName);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound compound = nbt.getCompoundTag("permissionAddon");

		NBTTagList groupsList = (NBTTagList)compound.getTag("groups");
		for(int i = 0; i < groupsList.tagCount(); i++) {
			Group g = Group.readFromNBT(groupsList.getCompoundTagAt(i));
			groups.add(g);
			if(g.isDefaultGroup()) {
				defaultGroup = g;
			}
		}
		
		NBTTagList allowList = (NBTTagList)compound.getTag("allow");
		for(int i = 0; i < allowList.tagCount(); i++) {
			NBTTagCompound entryTag = allowList.getCompoundTagAt(i);
			UUID uuid = UUID.fromString(entryTag.getString("uuid"));
			NBTTagList permList = (NBTTagList)entryTag.getTag("perms");
			Set<String> perms = Sets.newHashSet();
			for(int k = 0; k < permList.tagCount(); k++) {
				perms.add(permList.getStringTagAt(k));
			}
			playersSpecialPermissions.put(uuid, perms);
		}
		
		NBTTagList disallowList = (NBTTagList)compound.getTag("disallow");
		for(int i = 0; i < disallowList.tagCount(); i++) {
			NBTTagCompound entryTag = disallowList.getCompoundTagAt(i);
			UUID uuid = UUID.fromString(entryTag.getString("uuid"));
			NBTTagList permList = (NBTTagList)entryTag.getTag("perms");
			Set<String> perms = Sets.newHashSet();
			for(int k = 0; k < permList.tagCount(); k++) {
				perms.add(permList.getStringTagAt(k));
			}
			playersSpecialRestrictions.put(uuid, perms);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound compound = new NBTTagCompound();

		NBTTagList groupsList = new NBTTagList();
		for(Group g : groups) {
			NBTTagCompound groupTag = new NBTTagCompound();
			g.writeToNBT(groupTag);
			groupsList.appendTag(groupTag);
		}
		compound.setTag("groups", groupsList);
		
		NBTTagList allowList = new NBTTagList();
		for(Entry<UUID, Set<String>> entry : playersSpecialPermissions.entrySet()) {
			NBTTagCompound entryTag = new NBTTagCompound();
			entryTag.setString("uuid", entry.getKey().toString());
			NBTTagList permList = new NBTTagList();
			for(String perm : entry.getValue()) {
				permList.appendTag(new NBTTagString(perm));
			}
			entryTag.setTag("perms", permList);
			allowList.appendTag(entryTag);
		}
		compound.setTag("allow", allowList);
		
		NBTTagList disallowList = new NBTTagList();
		for(Entry<UUID, Set<String>> entry : playersSpecialRestrictions.entrySet()) {
			NBTTagCompound entryTag = new NBTTagCompound();
			entryTag.setString("uuid", entry.getKey().toString());
			NBTTagList permList = new NBTTagList();
			for(String perm : entry.getValue()) {
				permList.appendTag(new NBTTagString(perm));
			}
			entryTag.setTag("perms", permList);
			disallowList.appendTag(entryTag);
		}
		compound.setTag("disallow", disallowList);

		nbt.setTag("permissionAddon", compound);
	}

	public static void clear() {
		groups.clear();
		players.clear();
		playersSpecialPermissions.clear();
		playersSpecialRestrictions.clear();
	}

	public static void onServerStarting(FMLServerStartingEvent event) {
		if(ConfigFaction.ENABLE_PERMISSION) {
			clear();
			MapStorage storage = event.getServer().getEntityWorld().getMapStorage();
			PermissionManager data = (PermissionManager)storage.loadData(PermissionManager.class, "permissionAddon");
			if(data == null) {
				data = new PermissionManager("permissionAddon");
				storage.setData("permissionAddon", data);
			}
			INSTANCE = data;
		}
	}

	public static void registerPermission(String name) {
		permissions.add(name);
	}

	public static boolean hasPlayerPermission(String name, String permission) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(name);
		if(profile != null) {
			return hasEntityPermission(profile.getId(), permission);
		}
		return false;
	}

	public static boolean hasEntityPermission(UUID uuid, String permission) {
		if(!ConfigFaction.ENABLE_PERMISSION) {
			return true;
		}
		if(playersSpecialPermissions.get(uuid) != null && playersSpecialPermissions.get(uuid).contains(permission)) {
			return true;
		}
		if(playersSpecialRestrictions.get(uuid) != null && playersSpecialRestrictions.get(uuid).contains(permission)) {
			return false;
		}
		Group g = getPlayerGroup(uuid);
		if(g != null) {
			if(g.hasPermission(permission)) {
				return true;
			}
		} else {
			return isOperator(uuid);
		}
		return false;
	}

	public static boolean hasEntityPermission(Entity entity, String permission) {
		return hasEntityPermission(entity.getUniqueID(), permission);
	}

	public static EnumResult permissionList(String pattern) {
		if(pattern.matches("^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+)|(([a-zA-Z0-9]+\\.)*\\*)$")) {
			Set<PermissionBase> perms = PermissionManager.getPermission(pattern);
			if(perms.size() > 0) {
				String permsToString = "";
				for(PermissionBase base : perms) {
					permsToString += System.lineSeparator();
					permsToString += base.getPath();
				}
				return EnumResult.FOUND_PERMISSIONS.clear().addInformation(permsToString);
			}
			return EnumResult.NO_PERMISSION_FOUND.clear().addInformation(pattern);
		}
		return EnumResult.WRONG_SYNTAX;
	}

	public static Set<String> getGroupsNames() {
		Set<String> names = Sets.newHashSet();
		for(Group g : groups) {
			names.add(g.getName());
		}
		return names;
	}

	public static void onServerStarted(FMLServerStartedEvent event) {
		if(ConfigFaction.ENABLE_PERMISSION) {
			Map<String, ICommand> commands = MinecraftServer.getServer().getCommandManager().getCommands();
			for(Entry<String, ICommand> command : commands.entrySet()) {
				PermissionManager.registerPermission("command." + command.getKey());
			}		
		}
	}
	
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if(defaultGroup != null) {
			if(!isOperator(event.player.getName())) {
				setGroup(event.player.getName(), defaultGroup.getName());
			}
		}
		event.player.refreshDisplayName();
	}

	public static Set<String> getPaths(Set<PermissionBase> bases) {
		Set<String> ret = Sets.newHashSet();
		for(PermissionBase base : bases) {
			ret.add(base.getPath());
		}
		return ret;
	}
	
	public static EnumResult clearSpecialsPermissionsOrRestrictions(String name, boolean perms, boolean restricts) {
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(name);
		if(profile != null) {
			if(perms) {
				playersSpecialPermissions.remove(profile.getId());
				save();
			}
			if(restricts) {
				playersSpecialRestrictions.remove(profile.getId());
				save();
			}
			return EnumResult.SPECIALS_CLEARED.clear().addInformation(profile.getName());
		}
		return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(name);
	}
	
	public static EnumResult changeColor(String groupName, String colorName) {
		Group g = getGroupFromName(groupName);
		if(g != null) {
			EnumChatFormatting color = EnumChatFormatting.getValueByName(colorName);
			g.setColor(color);
			for(UUID uuid : g.getMembers()) {
				Entity entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
				if(entity instanceof EntityPlayer) {
					((EntityPlayer)entity).refreshDisplayName();
				}
			}
			save();
			return EnumResult.COLOR_CHANGED.clear().addInformation(g.getName()).addInformation(g.getColor().getFriendlyName());
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(groupName);
	}
	
	public static EnumResult changePrefix(String groupName, String prefix) {
		Group g = getGroupFromName(groupName);
		if(g != null) {
			g.setPrefix(prefix);
			for(UUID uuid : g.getMembers()) {
				Entity entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
				if(entity instanceof EntityPlayer) {
					((EntityPlayer)entity).refreshDisplayName();
				}
			}
			save();
			return EnumResult.PREFIX_CHANGED.clear().addInformation(g.getName()).addInformation(g.getPrefix());
		}
		return EnumResult.NOT_EXISTING_GROUP.clear().addInformation(groupName);
	}
}
