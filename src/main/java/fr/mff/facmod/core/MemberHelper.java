package fr.mff.facmod.core;

import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;
import net.minecraft.entity.player.EntityPlayer;

public class MemberHelper {
	
	/**
	 * Return player's {@link Member} object in the members' list
	 * @param player
	 * @param faction
	 * @return {@code null} if the player has not been found in members' list
	 */
	public static Member getMember(EntityPlayer player, Faction faction) {
		for(Member m : faction.getMembers()) {
			if(m.getUUID().equals(player.getUniqueID())) {
				return m;
			}
		}
		return null;
	}

}
