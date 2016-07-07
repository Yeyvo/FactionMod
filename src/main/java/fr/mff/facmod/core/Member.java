package fr.mff.facmod.core;

import java.util.Comparator;
import java.util.UUID;

import fr.mff.facmod.network.PacketHelper;
import net.minecraft.nbt.NBTTagCompound;

public class Member {
	
	protected final UUID uuid;
	protected EnumRank rank;
	
	public Member(UUID uuid, EnumRank rank) {
		this.uuid = uuid;
		this.rank = rank;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public EnumRank getRank() {
		return this.rank;
	}
	
	public void setRank(EnumRank rank) {
		this.rank = rank;
		PacketHelper.updateClientRank(this.getUUID());
	}
	
	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("uuid", this.uuid.toString());
		compound.setString("rank", this.rank.name());
	}
	
	public static Member readFromNBT(NBTTagCompound compound) {
		UUID uuid = UUID.fromString(compound.getString("uuid"));
		EnumRank rank = EnumRank.valueOf(compound.getString("rank"));
		return new Member(uuid, rank);
	}
	
	
	public static class MemberComparator implements Comparator<Member> {

		@Override
		public int compare(Member o1, Member o2) {
			return o1.getRank().getAutority() - o2.getRank().getAutority();
		}
		
	}

}
