package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;

public class FactionHelper {

	/**
	 * Return player's faction
	 * @param player
	 * @return {@code null} if the player doesn't have a faction <br /> 
	 */
	public static Faction getPlayerFaction(UUID uuid) {
		return FactionHelper.getFactionFromName(SystemHandler.getPlayers().get(uuid));
	}

	/**
	 * Check if the player is able to join a faction
	 * @param player
	 * @return {@code true} if the player comme join a faction
	 */
	public static boolean canPlayerJoinFaction(UUID uuid) {
		return FactionHelper.getPlayerFaction(uuid) == null;
	}

	/**
	 * Creates a faction
	 * @param player Faction's owner
	 * @param factionName
	 * @param factionDescription
	 * @return {@code true} if the faction has been created
	 */
	public static boolean tryCreateFaction(UUID uuid, String factionName, String factionDescription) {
		if(FactionHelper.canPlayerJoinFaction(uuid)) {
			if(FactionHelper.getFactionFromName(factionName) == null) {
				Faction faction = new Faction(factionName, factionDescription, uuid);
				return SystemHandler.addFaction(faction);
			}
		}
		return false;
	}

	/**
	 * Sets player's faction
	 * @param player
	 * @param faction
	 * @return {@code false} if player's faction has not changed
	 */
	public static boolean setPlayerFaction(UUID uuid, Faction faction) {
		if(FactionHelper.canPlayerJoinFaction(uuid)) {
			SystemHandler.setPlayer(uuid, faction.getName());
		}
		return false;
	}

	/**
	 * Returns the faction with the given name
	 * @param name
	 * @return {@code null} if the faction doesn't exist
	 */
	public static Faction getFactionFromName(String name) {
		for(Faction faction : SystemHandler.getAllFactions()) {
			if(faction.getName().equalsIgnoreCase(name)) {
				return faction;
			}
		}
		return null;
	}

	/**
	 * Updates ExtendedEntityProperties when a player leave a faction
	 * @param player
	 * @return {@code false} if the player wasn't in a faction
	 */
	public static boolean setNoFaction(UUID uuid) {
		Faction faction = FactionHelper.getPlayerFaction(uuid);
		if(faction != null) {
			SystemHandler.setPlayer(uuid, "");
			return true;
		}
		return false;
	}

	/**
	 * Updates faction's situation
	 * @param faction
	 * @return {@code false} if the faction has been removed
	 */
	public static boolean updateFaction(Faction faction) {
		if(faction.getMembers().size() == 0) {
			return !SystemHandler.removeFaction(faction);
		}
		return true;
	}

	/**
	 * Chooses a new owner for the faction, choose highest and oldest member
	 * @param faction
	 */
	public static void chooseNewOwner(Faction faction) {
		int highestRank = EnumRank.values().length;
		List<Member> promotableMembers = new ArrayList<Member>();
		for(Member member : faction.getMembers()) {
			int degree = member.getRank().getDegree();
			if(degree < highestRank) {
				highestRank = degree;
				promotableMembers.clear();
			}
			if(degree == highestRank) {
				promotableMembers.add(member);
			}
		}
		promotableMembers.get(0).setRank(EnumRank.OWNER);
	}

	/**
	 * Removes all faction's members
	 * @param faction
	 * @return {@code true} if the faction has been removed
	 */
	public static boolean clearFaction(Faction faction) {
		for(Member member : faction.getMembers()) {
			FactionHelper.setNoFaction(member.getUUID());
		}
		return FactionHelper.updateFaction(faction);
	}

}
