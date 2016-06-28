package fr.mff.facmod.core;

import java.util.UUID;

import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;

public class MemberHelper {
	
	/**
	 * Return player's {@link Member} object in the members' list
	 * @param player
	 * @param faction
	 * @return {@code null} if the player has not been found in members' list
	 */
	public static Member getMember(UUID uuid, Faction faction) {
		for(Member m : faction.getMembers()) {
			if(m.getUUID().equals(uuid)) {
				return m;
			}
		}
		return null;
	}

}
