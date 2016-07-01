package fr.mff.facmod.core;

public enum EnumResult {
	
	FACTION_JOINED("result.factionJoined", true),
	FACTION_CREATED("result.factionCreated", true),
	FACTION_LEFT("result.factionLeft", true),
	
	BANNED_PLAYER("result.bannedPlayer", false),
	ALREADY_BANNED_PLAYER("result.alreadyBannedPlayer", false),
	
	PLAYER_ADDED("result.playerAdded", true),
	
	LAND_CLAIMED("result.landClaimed", true),
	ALREADY_CLAIMED_LAND("result.alreadyClaimedLand", false),
	NOT_IN_A_FACTION("result.notInAFaction", false),
	
	IN_A_FACTION("result.inAFaction", false),
	NOT_EXISTING_FACTION("result.notExistingFaction", false),
	INVITATION_NEEDED("result.invitationNeeded", false),
	
	TAKEN_FACTION_NAME("result.takenName", false),
	INVALID_NAME_LENGTH("result.invalidNameLength", false),
	INVALID_DESCRIPTION_LENGTH("result.invalidDescriptionLength", false),
	
	NO_PERMISSION("result.noPermission", false);
	
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
