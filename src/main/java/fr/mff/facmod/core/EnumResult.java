package fr.mff.facmod.core;

public enum EnumResult {
	
	FACTION_JOINED("result.factionJoined", true),
	FACTION_CREATED("result.factionCreated", true),
	FACTION_LEFT("result.factionLeft", true),
	FACTION_OPENED("result.factionOpened", true),
	FACTION_CLOSED("result.factionClosed", true),
	FACTION_REMOVED("result.factionRemoved", true),
	
	BANNED_PLAYER("result.bannedPlayer", false),
	PLAYER_BANNED("result.playerBanned", true),
	ALREADY_BANNED_PLAYER("result.alreadyBannedPlayer", false),
	
	PLAYER_ADDED("result.playerAdded", true),
	NOT_EXISTING_PLAYER("result.notExistingPlayer", false),
	
	LAND_CLAIMED("result.landClaimed", true),
	ALREADY_CLAIMED_LAND("result.alreadyClaimedLand", false),
	NOT_CLAIMED_LAND("result.notClaimedLand", false),
	CLAIMED_BY_FACTION("result.claimedByFaction", false),
	LAND_UNCLAIMED("result.landUnclaimed", true),
	NOT_IN_A_FACTION("result.notInAFaction", false),
	
	IN_A_FACTION("result.inAFaction", false),
	NOT_EXISTING_FACTION("result.notExistingFaction", false),
	INVITATION_NEEDED("result.invitationNeeded", false),
	
	PLAYER_NOT_IN_THE_FACTION("result.playerNotInTheFaction", false),
	PLAYER_KICKED("result.playerKicked", true),
	
	TAKEN_FACTION_NAME("result.takenName", false),
	INVALID_NAME_LENGTH("result.invalidNameLength", false),
	INVALID_DESCRIPTION_LENGTH("result.invalidDescriptionLength", false),
	DESCRIPTION_CHANGED("result.descriptionChanged", true),
	
	NO_PERMISSION("result.noPermission", false),
	
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
