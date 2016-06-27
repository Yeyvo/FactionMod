package fr.mff.facmod.core.features;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import fr.mff.facmod.core.FactionHelper;
import fr.mff.facmod.core.MemberHelper;

public class Faction {

	/** Faction's name */
	protected String factionName;
	/** Faction's description */
	protected String factionDesc;

	protected List<Member> members = new ArrayList<Member>();
	protected List<UUID> bannedPlayers = new ArrayList<UUID>();

	public Faction(String facName, String desc, EntityPlayer player) {
		this.factionName = facName;
		this.factionDesc = desc;
		members.add(new Member(player, EnumRank.OWNER));
		FactionHelper.setPlayerFaction(player, this);
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

	/**
	 * Removes a player from the faction
	 * @param player
	 * @return {@code true} if the player has been removed
	 */
	public boolean removePlayer(EntityPlayer player) {
		if(FactionHelper.getPlayerFaction(player).equals(this)) {
			Member member = MemberHelper.getMember(player, this);
			if(member != null) {
				members.remove(member);
				FactionHelper.setNoFaction(player);
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
	 * @param player
	 * @return {@code false} if the player can't be banned
	 */
	public boolean banPlayer(EntityPlayer player) {
		if(bannedPlayers.contains(player.getUniqueID())) return false;
		bannedPlayers.add(player.getUniqueID());
		this.removePlayer(player);
		return true;
	}

	/**
	 * Adds a player
	 * @param player
	 * @return {@code false} if the player can't be added
	 */
	public boolean addPlayer(EntityPlayer player) {
		if(bannedPlayers.contains(player.getUniqueID())) return false;
		if(FactionHelper.canPlayerJoinFaction(player)) {
			return FactionHelper.setPlayerFaction(player, this);
		}
		return false;
	}

}
