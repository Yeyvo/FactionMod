package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;

public class FactionHelper {

	/**
	 * Returns player's faction
	 * @param player
	 * @return {@code null} if the player doesn't have a faction <br /> 
	 */
	public static Faction getPlayerFaction(UUID uuid) {
		return FactionHelper.getFactionFromName(SystemHandler.getPlayers().get(uuid));
	}

	/**
	 * Checks if the player is able to join a faction
	 * <ul>
	 * 		<li>Returns false if the player is in a faction</li>
	 * </ul>
	 * @param player
	 * @return {@code true} if the player can join a faction
	 */
	public static boolean canPlayerJoinFaction(UUID uuid) {
		return FactionHelper.getPlayerFaction(uuid) == null;
	}

	/**
	 * Creates a faction
	 * <ul>
	 * 		<li>Checks if the player can join a faction using {@link FactionHelper#canPlayerJoinFaction(UUID)}</li>
	 * 		<li>Checks if a faction with the given name doesn't exist
	 * 		<li>Creates a new instance of {@link Faction}
	 * 		<li>Adds it to factions list using {@link SystemHandler#addFaction(Faction)}
	 * </ul>
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
	 * <ul>
	 * 		<li>Checks if the player can join a faction using {@link FactionHelper#canPlayerJoinFaction(UUID)}</li>
	 * 		<li>Sets player's faction using {@link SystemHandler#setPlayer(UUID, String)}</li>
	 * </ul>
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
	 * Change player's faction to ""
	 * <ul>
	 * 		<li>Checks if the player is in a faction</li>
	 * 		<li>Sets player's faction to "" using {@link SystemHandler#setPlayer(UUID, String)}</li>
	 * </ul>
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
	 * Check if the the faction has member(s)
	 * <ul>
	 * 		<li>Checks if the faction's members list's size equals to 0</li>
	 * 		<li>Removes faction to factions list using {@link SystemHandler#removeFaction(Faction)}</li>
	 * </ul>
	 * @param faction
	 * @return {@code true} if the faction has member(s)
	 */
	public static boolean hasMember(Faction faction) {
		if(faction.getMembers().size() == 0) {
			return !SystemHandler.removeFaction(faction);
		}
		return true;
	}

	/**
	 * Chooses a new owner for the faction
	 * <ul>
	 * 		<li>Parses faction's members to set highest ranked member to faction's owner</li>
	 * </ul>
	 * @param faction
	 */
	public static void chooseNewOwner(Faction faction) {
		int highestRank = EnumRank.values().length;
		List<Member> promotableMembers = new ArrayList<Member>();
		for(Member member : faction.getMembers()) {
			int degree = member.getRank().getAutority();
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
	 * <ul>
	 * 		<li>Uses {@link FactionHelper#setNoFaction(UUID)} on each faction's member</li>
	 * 		<li>Update faction using {@link FactionHelper#hasMember(Faction)}</li>
	 * </ul>
	 * @param faction
	 * @return {@code true} if the faction has been removed
	 */
	public static boolean clearFaction(Faction faction) {
		for(Member member : faction.getMembers()) {
			FactionHelper.setNoFaction(member.getUUID());
		}
		return FactionHelper.hasMember(faction);
	}

}
