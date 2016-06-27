package fr.mff.facmod.core;

import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;
import net.minecraft.entity.player.EntityPlayer;

public class RankHelper {
	
	public static boolean canAffect(EntityPlayer executor, EntityPlayer slave) {
		return RankHelper.getRank(executor).getDegree() < RankHelper.getRank(slave).getDegree();
	}
	
	public static EnumRank getRank(EntityPlayer player) {
		Faction faction = FactionHelper.getPlayerFaction(player);
		if(faction == null) return EnumRank.WITHOUT_FACTION;
		for(Member member : faction.getMembers()) {
			if(member.getUUID().equals(player.getUniqueID())) {
				return member.getRank();
			}
		}
		return EnumRank.WITHOUT_FACTION;
	}

}
