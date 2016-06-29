package fr.mff.facmod.core.features;

import java.util.ArrayList;
import java.util.List;

import fr.mff.facmod.core.permissions.Permission;

/**
 * 	Contains all faction's grades
 */
public enum EnumRank {
	
	/** The owner of the faction, has all permissions */
	OWNER(4, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING, Permission.FACTION_HANDLING}),
	/** A member managing the community */
	COMMUNITY_MANAGER(3, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING}),
	/** A basic member (default rank) */
	NEWBIE(2, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK}),
	/** A member who is able to move in the base */
	VISITOR(1, new Permission[]{Permission.USE_BLOCK}),
	/** A player who is not in a faction */
	WITHOUT_FACTION(0, new Permission[]{});
	
	
	private int autority;
	private List<Permission> permissions = new ArrayList<Permission>();
	
	/**
	 * @param degree Lowest degrees are the best ranks
	 * @param perms Permissions for this grade
	 */
	private EnumRank(int degree, Permission[] perms) {
		this.autority = degree;
		for(Permission p : perms) {
			this.permissions.add(p);
		}
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
	
	public static EnumRank fromDegree(int degree) {
		for(EnumRank grade : EnumRank.values()) {
			if(grade.getAutority() == degree) {
				return grade;
			}
		}
		return null;
	}

}
