package fr.mff.facmod.core.features;

import java.util.UUID;

public class Member {
	
	private UUID uuid;
	private EnumRank rank;
	
	public Member(UUID player, EnumRank rank) {
		this.uuid = player;
		this.rank = rank;
	}

	public EnumRank getRank() {
		return rank;
	}

	public void setRank(EnumRank rank) {
		this.rank = rank;
	}

	public UUID getUUID() {
		return uuid;
	}

}
