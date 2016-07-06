package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

/**
 * 	Contains all faction's grades
 */
public enum EnumRank {

	/** The owner of the faction, has all permissions */
	OWNER(4, "owner", EnumChatFormatting.RED, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING, Permission.FACTION_HANDLING, Permission.LAND_HANDLING}),
	/** A member managing members and lands */
	MANAGER(3, "manager", EnumChatFormatting.BLUE, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING, Permission.LAND_HANDLING}),
	/** A member managing the community */
	COMMUNITY_MANAGER(2, "communityManager", EnumChatFormatting.GREEN, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING}),
	/** A member managing lands */
	LAND_MANAGER(2, "landManager", EnumChatFormatting.GREEN, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.LAND_HANDLING}),
	/** A basic member (default rank) */
	BASIC_MEMBER(1, "basicMember", EnumChatFormatting.WHITE, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK}),
	/** A member who is able to move in the base */
	WITHOUT_FACTION(0, "", EnumChatFormatting.GRAY, new Permission[]{});


	private int autority;
	private List<Permission> permissions = new ArrayList<Permission>();
	private String translationKey;
	private EnumChatFormatting formatting;

	/**
	 * @param degree Lowest degrees are the best ranks
	 * @param perms Permissions for this grade
	 */
	private EnumRank(int degree, String translationKey, EnumChatFormatting formatting, Permission[] perms) {
		this.autority = degree;
		this.translationKey = translationKey;
		this.formatting = formatting;
		for(Permission p : perms) {
			this.permissions.add(p);
		}
	}
	
	public String getDisplay() {
		return I18n.format("rank." + translationKey, new Object[0]);
	}
	
	public EnumChatFormatting getColor() {
		return this.formatting;
	}

	public int getAutority() {
		return this.autority;
	}

	/**
	 * @param perm The permission to test
	 * @return {@code true} if the player has the given permission
	 */
	public boolean hasPermission(Permission perm) {
		return permissions.contains(perm);
	}

	public List<Permission> getPermissions() {
		return this.permissions;
	}

	/**
	 * Checks if the {@code executor} can influence the {@code slave}
	 * <ul>
	 * 		<li>Check if executor's autoriy is superior to slave's one</li>
	 * </ul>
	 * @param executor
	 * @param slave
	 * @return
	 */
	public static boolean canAffect(UUID executor, UUID slave) {
		return EnumRank.getRank(executor).getAutority() > EnumRank.getRank(slave).getAutority();
	}

	/**
	 * Returns player's rank
	 * @param uuid
	 * @return {@link EnumRank#WITHOUT_FACTION} if the player is not in a faction
	 */
	public static EnumRank getRank(UUID uuid) {
		Faction faction = Faction.Registry.getPlayerFaction(uuid);
		if(faction == null) return EnumRank.WITHOUT_FACTION;
		for(Member member : faction.getMembers()) {
			if(member.getUUID().equals(uuid)) {
				return member.getRank();
			}
		}
		return EnumRank.WITHOUT_FACTION;
	}

}
