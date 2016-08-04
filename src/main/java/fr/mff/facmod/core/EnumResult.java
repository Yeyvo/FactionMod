package fr.mff.facmod.core;

public enum EnumResult {
	
	//Faction
	FACTION_JOINED("result.factionJoined", true),
	FACTION_CREATED("result.factionCreated", true),
	FACTION_LEFT("result.factionLeft", true),
	FACTION_OPENED("result.factionOpened", true),
	FACTION_CLOSED("result.factionClosed", true),
	FACTION_REMOVED("result.factionRemoved", true),
	BANNED_PLAYER("result.bannedPlayer", false),
	PLAYER_BANNED("result.playerBanned", true),
	ALREADY_BANNED_PLAYER("result.alreadyBannedPlayer", false),
	PLAYER_INVITED("result.playerInvited", true),
	ALREADY_INVITED_PLAYER("result.alreadyInvitedPlayer", false),
	IN_THE_FACTION("result.inTheFaction", false),
	PLAYER_ADDED("result.playerAdded", true),
	NOT_EXISTING_PLAYER("result.notExistingPlayer", false),
	NOT_CONNECTED_PLAYER("result.notConnectedPlayer", false),
	LAND_CLAIMED("result.landClaimed", true),
	ALREADY_CLAIMED_LAND("result.alreadyClaimedLand", false),
	LAND_OF_THE_FACTION("result.landOfTheFaction", false),
	NOT_CLAIMED_LAND("result.notClaimedLand", false),
	LAND_UNCLAIMED("result.landUnclaimed", true),
	NOT_IN_A_FACTION("result.notInAFaction", false),
	IN_A_FACTION("result.inAFaction", false),
	HOME_SET("result.homeSet", true),
	NOT_EXISTING_FACTION("result.notExistingFaction", false),
	INVITATION_NEEDED("result.invitationNeeded", false),
	PLAYER_NOT_IN_THE_FACTION("result.playerNotInTheFaction", false),
	PLAYER_KICKED("result.playerKicked", true),
	TAKEN_FACTION_NAME("result.takenName", false),
	INVALID_NAME_LENGTH("result.invalidNameLength", false),
	INVALID_DESCRIPTION_LENGTH("result.invalidDescriptionLength", false),
	DESCRIPTION_CHANGED("result.descriptionChanged", true),
	NO_PERMISSION("result.noPermission", false),
	TP_LAUNCHED("result.tpLaunched", true),
	HOME_NOT_SET("result.homeNotSet", false),
	PLAYER_PROMOTED("result.playerPromoted", true),
	WRONG_RANK("result.wrongRank", false),
	WRONG_WORLD("result.wrongWorld", false),
	IN_A_SAFE_ZONE("result.inASafeZone", false),
	IN_A_WAR_ZONE("result.inAWarZone", false),
	REMOVED_SAFE_ZONE("result.removedSafeZone", true),
	REMOVED_WAR_ZONE("result.removedWarZone", true),
	NOT_WAR_OR_SAFE_ZONE("result.notWarOrSafeZone", false),
	SAFE_ZONE_SET("result.safeZoneSet", true),
	WAR_ZONE_SET("result.warZoneSet", true),
	NOT_ENOUGTH_POWER("result.notEnougthPower", false),
	MAX_CLAIMS_REACHED("result.maxClaimsReached", false),
	PLAYER_UNBANNED("result.playerUnbanned", true),
	PLAYER_NOT_BANNED("result.playerNotBanned", false),
	
	//Permission
	GROUP_CREATED("result.groupCreated", true),
	EXISTING_GROUP("result.existingGroup", false),
	NOT_EXISTING_GROUP("result.notExistingGroup", false),
	GROUP_REMOVED("result.groupRemoved", true),
	PLAYER_ADDED_TO_GROUP("result.playerAddedToGroup", true),
	PLAYER_ALREADY_IN_THE_GROUP("result.playerAlreadyInTheGroup", false),
	WRONG_SYNTAX("result.wrongSyntax", false),
	PERMISSION_ADDED("result.permissionAdded", true),
	PERMISSION_REMOVED("result.permissionRemoved", true),
	INVALID_NAME("result.invalidName", false),
	NO_PERMISSION_FOUND("result.noPermissionFound", false),
	FOUND_PERMISSIONS("result.foundPermissions", true),
	PLAYER_REMOVED_FROM_GROUP("result.playerRemovedFromGroup", true),
	PLAYER_NOT_IN_A_GROUP("result.playerNotInAGroup", false),
	SPECIAL_PERMISSION_ADDED("result.specialPermissionAdded", true),
	SPECIAL_RESTRICTION_ADDED("result.specialRestrictionAdded", true),
	SPECIALS_CLEARED("result.specialsCleared", true),
	COLOR_CHANGED("result.colorChanged", true),
	PREFIX_CHANGED("result.prefixChanged", true),
	DEFAULT_GROUP_CHANGED("result.defaultGroupChanged", true),
	
	ERROR("result.error", false);
	
	private String languageKey;
	private boolean success;
	private Object[] informations = new Object[0];
	
	private EnumResult(String languageKey, boolean success) {
		this.languageKey = languageKey;
		this.success = success;
	}
	
	public String getLanguageKey() {
		return this.languageKey;
	}
	
	public boolean isSuccess() {
		return this.success;
	}
	
	public EnumResult addInformation(Object o) {
		Object[] objects = new Object[this.informations.length + 1];
		for(int i = 0; i < this.informations.length; i++) {
			objects[i] = informations[i];
		}
		objects[objects.length - 1] = o;
		this.informations = objects;
		return this;
	}
	
	public Object[] getInformations() {
		return this.informations;
	}
	
	public EnumResult clear() {
		this.informations = new Object[0];
		return this;
	}

}
