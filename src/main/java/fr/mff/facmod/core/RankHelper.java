package fr.mff.facmod.core;

import java.util.UUID;

import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;

public class RankHelper {
	
	/**
	 * Checks if the {@code executor} can influence the {@code slave}
	 * <ul>
	 * 		<li>Check if executor's autoriy is superior to slave's one</li>
	 * </ul>
	 * @param executor
	 * @param slave
	 * @return
	 */
	public static boolean canAffect(UUID executor, UUID slave) {
		return RankHelper.getRank(executor).getAutority() > RankHelper.getRank(slave).getAutority();
	}
	
	/**
	 * Returns player's rank
	 * @param uuid
	 * @return {@link EnumRank#WITHOUT_FACTION} if the player is not in a faction
	 */
	public static EnumRank getRank(UUID uuid) {
		Faction faction = FactionHelper.getPlayerFaction(uuid);
		if(faction == null) return EnumRank.WITHOUT_FACTION;
		for(Member member : faction.getMembers()) {
			if(member.getUUID().equals(uuid)) {
				return member.getRank();
			}
		}
		return EnumRank.WITHOUT_FACTION;
	}

}
