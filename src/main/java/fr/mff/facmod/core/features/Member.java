package fr.mff.facmod.core.features;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class Member {
	
	private EntityPlayer player;
	private EnumRank rank;
	
	public Member(EntityPlayer player, EnumRank rank) {
		this.player = player;
		this.rank = rank;
	}

	public EnumRank getRank() {
		return rank;
	}

	public void setRank(EnumRank rank) {
		this.rank = rank;
	}

	public UUID getUUID() {
		return player.getUniqueID();
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}

}
