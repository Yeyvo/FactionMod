package fr.mff.facmod.perm;

import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Sets;

public class Group {

	private String name;
	private String prefix;
	private EnumChatFormatting color;
	private Set<String> permissions = Sets.newHashSet();
	private Set<UUID> members = Sets.newHashSet();

	public Group(String name, String prefix, EnumChatFormatting prefixColor) {
		this.name = name;
		this.prefix = prefix;
		this.color = prefixColor;
	}

	public String getName() {
		return this.name;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getDisplayPrefix() {
		return this.color + "[" + this.getPrefix() + "]";
	}
	
	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}
	
	public void addMember(UUID uuid) {
		members.add(uuid);
		PermissionManager.players.put(uuid, this);
		PermissionManager.save();
	}

	public void addPermission(String perm) {
		permissions.add(perm);
		PermissionManager.save();
	}
	
	public void removeMember(UUID uuid) {
		members.remove(uuid);
		PermissionManager.players.remove(uuid);
		PermissionManager.save();
	}
	
	public void removePermission(String perm) {
		permissions.remove(perm);
		PermissionManager.save();
	}
	
	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("name", name);
		compound.setString("prefix", prefix);
		compound.setString("color", color.name());
		
		NBTTagList permissionsList = new NBTTagList();
		for(String permission : permissions) {
			permissionsList.appendTag(new NBTTagString(permission));
		}
		compound.setTag("permissions", permissionsList);
		
		NBTTagList membersList = new NBTTagList();
		for(UUID uuid : members) {
			membersList.appendTag(new NBTTagString(uuid.toString()));
		}
		compound.setTag("members", membersList);
	}
	
	public static Group readFromNBT(NBTTagCompound compound) {
		Group g = new Group(compound.getString("name"), compound.getString("prefix"), EnumChatFormatting.valueOf(compound.getString("color")));
		
		NBTTagList permissionsList = (NBTTagList)compound.getTag("permissions");
		for(int i = 0; i < permissionsList.tagCount(); i++) {
			g.addPermission(permissionsList.getStringTagAt(i));
		}
		
		NBTTagList membersList = (NBTTagList)compound.getTag("members");
		for(int i = 0; i < membersList.tagCount(); i++) {
			g.addMember(UUID.fromString(membersList.getStringTagAt(i)));
		}
		
		return g;
	}

	@Override
	public boolean equals(Object g) {
		if(g instanceof Group) {
			return ((Group)g).getName().equalsIgnoreCase(this.getName());
		}
		return false;
	}

}
