package fr.mff.facmod.core.features;

import java.util.ArrayList;
import java.util.List;

import fr.mff.facmod.core.permissions.Permission;

/**
 * 	Contains all faction's grades
 */
public enum EnumRank {
	
	OWNER(0, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING, Permission.FACTION_HANDLING}),
	COMMUNITY_MANAGER(1, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK, Permission.COMMUNITY_HANDLING}),
	NEWBIE(2, new Permission[]{Permission.ALTER_BLOCK, Permission.USE_BLOCK}),
	VISITOR(3, new Permission[]{Permission.USE_BLOCK}),
	WITHOUT_FACTION(4, new Permission[]{});
	
	
	private int degree;
	private List<Permission> permissions = new ArrayList<Permission>();
	
	/**
	 * @param degree Lowest degrees are the best ranks
	 * @param perms Permissions for this grade
	 */
	private EnumRank(int degree, Permission[] perms) {
		this.degree = degree;
		for(Permission p : perms) {
			this.permissions.add(p);
		}
	}
	
	public int getDegree() {
		return this.degree;
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
			if(grade.getDegree() == degree) {
				return grade;
			}
		}
		return null;
	}

}
