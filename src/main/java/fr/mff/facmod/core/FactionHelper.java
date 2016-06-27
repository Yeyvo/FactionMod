package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import fr.mff.facmod.core.extendedProperties.ExtendedPropertieFaction;
import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;

public class FactionHelper {
	
	/**
	 * Return player's faction
	 * @param player
	 * @return {@code null} if the player doesn't have a faction <br /> 
	 */
	public static Faction getPlayerFaction(EntityPlayer player) {
		return FactionHelper.getFactionFromName(ExtendedPropertieFaction.get(player).factionName);
	}
	
	/**
	 * Check if the player is able to join a faction
	 * @param player
	 * @return {@code true} if the player comme join a faction
	 */
	public static boolean canPlayerJoinFaction(EntityPlayer player) {
		return FactionHelper.getPlayerFaction(player) == null;
	}
	
	/**
	 * Creates a faction
	 * @param player Faction's owner
	 * @param factionName
	 * @param factionDescription
	 * @return {@code true} if the faction has been created
	 */
	public static boolean createFaction(EntityPlayer player, String factionName, String factionDescription) {
		if(FactionHelper.canPlayerJoinFaction(player)) {
			Faction faction = new Faction(factionName, factionDescription, player);
			return SystemHandler.addFaction(faction);
		}
		return false;
	}
	
	/**
	 * Sets player's faction
	 * @param player
	 * @param faction
	 * @return {@code false} if player's faction has not changed
	 */
	public static boolean setPlayerFaction(EntityPlayer player, Faction faction) {
		ExtendedPropertieFaction prop = ExtendedPropertieFaction.get(player);
		if(prop.factionName.equals("")) {
			prop.setFaction(faction);
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
			if(faction.getName().toUpperCase().equals(name.toUpperCase())) {
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
	public static boolean setNoFaction(EntityPlayer player) {
		Faction faction = FactionHelper.getPlayerFaction(player);
		if(faction != null) {
			ExtendedPropertieFaction prop = ExtendedPropertieFaction.get(player);
			prop.setNoFaction();
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
			FactionHelper.setNoFaction(member.getPlayer());
		}
		return FactionHelper.updateFaction(faction);
	}

}
