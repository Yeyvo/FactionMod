package fr.mff.facmod.core;

import java.util.UUID;

import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;

public class RankHelper {
	
	public static boolean canAffect(UUID executor, UUID slave) {
		return RankHelper.getRank(executor).getDegree() < RankHelper.getRank(slave).getDegree();
	}
	
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
