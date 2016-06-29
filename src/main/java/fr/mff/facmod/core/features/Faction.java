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

	/**
	 * Creates a faction
	 * <ul>
	 * 		<li>Uses {@link FactionHelper#setPlayerFaction(UUID, Faction)} on the owner</li>
	 * </ul>
	 * @param facName
	 * @param desc
	 * @param uuid
	 */
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
	 * <ul>
	 * 		<li>Checks is the player's faction is this</li>
	 * 		<li>Removes member from the faction</li>
	 * 		<li>Sets player no faction using {@link FactionHelper#setNoFaction(UUID)}
	 * 		<li>Checks if the faction has member</li>
	 * 		<li>Checks if the removed member was the owner</li>
	 * 		<li>Chooses an other owner using {@link FactionHelper#chooseNewOwner(Faction)}</li>
	 * </ul>
	 * @param UUID
	 * @return {@code true} if the player has been removed
	 */
	public boolean removePlayer(UUID uuid) {
		if(FactionHelper.getPlayerFaction(uuid) != null && FactionHelper.getPlayerFaction(uuid).equals(this)) {
			Member member = MemberHelper.getMember(uuid, this);
			if(member != null) {
				members.remove(member);
				FactionHelper.setNoFaction(uuid);
				if(FactionHelper.hasMember(this)) {
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
	 * <ul>
	 * 		<li>Checks if the player is not banned</li>
	 * 		<li>Add the player to the banned players list</li>
	 * 		<li>Remove the player from the faction using {@link Faction#removePlayer(UUID)}</li>
	 * </ul>
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
	 * <ul>
	 * 		<li>Checks if the player is not banned</li>
	 * 		<li>Sets player's faction to this using {@link FactionHelper#setPlayerFaction(UUID, Faction)}
	 * </ul>
	 * @param player
	 * @return {@code false} if the player can't be added
	 */
	public boolean addPlayer(UUID uuid) {
		if(bannedPlayers.contains(uuid)) return false;
		return FactionHelper.setPlayerFaction(uuid, this);
	}

}
