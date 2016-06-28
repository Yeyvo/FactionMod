package fr.mff.facmod.core.features;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.mff.facmod.core.FactionHelper;
import fr.mff.facmod.core.MemberHelper;

public class Faction {

	/** Faction's name */
	protected String factionName;
	/** Faction's description */
	protected String factionDesc;

	protected List<Member> members = new ArrayList<Member>();
	protected List<UUID> bannedPlayers = new ArrayList<UUID>();

	public Faction(String facName, String desc, UUID uuid) {
		this.factionName = facName;
		this.factionDesc = desc;
		members.add(new Member(uuid, EnumRank.OWNER));
		FactionHelper.setPlayerFaction(uuid, this);
	}

	public Faction(String facName, String desc, List<Member> members, List<UUID> bannedPlayers) {
		this.factionName = facName;
		this.factionDesc = desc;
		this.members = members;
		this.bannedPlayers = bannedPlayers;
	}

	public String getDescription() {
		return factionDesc;
	}

	public void setDescription(String factionDesc) {
		this.factionDesc = factionDesc;
	}

	public String getName() {
		return factionName;
	}

	public List<Member> getMembers() {
		return members;
	}

	public List<UUID> getBannedPlayers() {
		return bannedPlayers;
	}

	/**
	 * Removes a player from the faction
	 * @param UUID
	 * @return {@code true} if the player has been removed
	 */
	public boolean removePlayer(UUID uuid) {
		if(FactionHelper.getPlayerFaction(uuid).equals(this)) {
			Member member = MemberHelper.getMember(uuid, this);
			if(member != null) {
				members.remove(member);
				FactionHelper.setNoFaction(uuid);
				if(FactionHelper.updateFaction(this)) {
					if(member.getRank().equals(EnumRank.OWNER)) {
						FactionHelper.chooseNewOwner(this);
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Bans a player
	 * @param uuid
	 * @return {@code false} if the player can't be banned
	 */
	public boolean banPlayer(UUID uuid) {
		if(bannedPlayers.contains(uuid)) return false;
		bannedPlayers.add(uuid);
		this.removePlayer(uuid);
		return true;
	}

	/**
	 * Adds a player
	 * @param player
	 * @return {@code false} if the player can't be added
	 */
	public boolean addPlayer(UUID uuid) {
		if(bannedPlayers.contains(uuid)) return false;
		if(FactionHelper.canPlayerJoinFaction(uuid)) {
			return FactionHelper.setPlayerFaction(uuid, this);
		}
		return false;
	}

}
