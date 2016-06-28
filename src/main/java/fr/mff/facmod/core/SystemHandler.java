package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldSavedData;
import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;
import fr.mff.facmod.network.PacketHelper;

public class SystemHandler extends WorldSavedData {

	private static SystemHandler INSTANCE;
	private final List<Faction> factions = new ArrayList<Faction>();
	private final HashMap<UUID, String> players = new HashMap<UUID, String>();

	public static List<Faction> getAllFactions() {
		return INSTANCE.factions;
	}
	
	public static HashMap<UUID, String> getPlayers() {
		return INSTANCE.players;
	}
	
	public static void setPlayer(UUID uuid, String factionName) {
		INSTANCE.players.put(uuid, factionName);
		PacketHelper.updateClient(uuid);
		INSTANCE.markDirty();
	}
	
	public static void removePlayer(UUID uuid) {
		INSTANCE.players.remove(uuid);
		INSTANCE.markDirty();
	}

	public static boolean removeFaction(Faction faction) {
		INSTANCE.markDirty();
		return INSTANCE.factions.remove(faction);
	}

	public static boolean addFaction(Faction faction) {
		INSTANCE.markDirty();
		return INSTANCE.factions.add(faction);
	}
	
	//--------------------Instance-------------------------//
	
	public SystemHandler(String name) {
		super(name);
		INSTANCE = this;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound compound = (NBTTagCompound)nbt.getTag("factionmod");
		NBTTagList playersList = (NBTTagList)compound.getTag("players");
		for(int i = 0; i < playersList.tagCount(); i++) {
			NBTTagCompound playerTag = playersList.getCompoundTagAt(i);
			SystemHandler.setPlayer(UUID.fromString(playerTag.getString("uuid")), playerTag.getString("faction"));
		}
		NBTTagList factionsList = (NBTTagList)compound.getTag("factions");
		for(int i = 0; i < factionsList.tagCount(); i++) {
			NBTTagCompound factionTag = factionsList.getCompoundTagAt(i);
			
			NBTTagList membersList = (NBTTagList)factionTag.getTag("members");
			List<Member> members = new ArrayList<Member>();
			for(int k = 0; k < membersList.tagCount(); k++) {
				NBTTagCompound memberTag = membersList.getCompoundTagAt(k);
				EnumRank rank = EnumRank.valueOf(memberTag.getString("rank"));
				UUID uuid = UUID.fromString(memberTag.getString("uuid"));
				Member member = new Member(uuid, rank);
				members.add(member);
			}
			
			NBTTagList bannedList = (NBTTagList)factionTag.getTag("bannedPlayers");
			List<UUID> bannedPlayers = new ArrayList<UUID>();
			for(int k = 0; k < bannedList.tagCount(); k++) {
				String uuid = bannedList.getStringTagAt(k);
				bannedPlayers.add(UUID.fromString(uuid));
			}
			
			String facName = factionTag.getString("factionName");
			String desc = factionTag.getString("factionDesc");
			Faction faction = new Faction(facName, desc, members, bannedPlayers);
			this.factions.add(faction);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList factionsList = new NBTTagList();
		
		for(Faction faction : this.factions) {
			
			NBTTagCompound factionTag = new NBTTagCompound();
			NBTTagList membersList = new NBTTagList();
			
			for(Member member : faction.getMembers()) {
				
				NBTTagCompound memberTag = new NBTTagCompound();
				memberTag.setString("uuid", member.getUUID().toString());
				memberTag.setString("rank", member.getRank().name());
				
				membersList.appendTag(memberTag);
			}
			factionTag.setTag("members", membersList);
			
			factionTag.setString("factionName", faction.getName());
			factionTag.setString("factionDesc", faction.getDescription());
			
			NBTTagList bannedList = new NBTTagList();
			for(UUID uuid : faction.getBannedPlayers()) {
				bannedList.appendTag(new NBTTagString(uuid.toString()));
			}
			factionTag.setTag("bannedPlayers", bannedList);
			
			factionsList.appendTag(factionTag);
		}
		
		NBTTagList playersList = new NBTTagList();
		
		Iterator<Entry<UUID, String>> iterator = this.players.entrySet().iterator();
		while(iterator.hasNext()) {
			NBTTagCompound playerTag = new NBTTagCompound();
			Entry<UUID, String> entry = iterator.next();
			playerTag.setString("uuid", entry.getKey().toString());
			playerTag.setString("faction", entry.getValue());

			playersList.appendTag(playerTag);
		}
		
		compound.setTag("factions", factionsList);
		compound.setTag("players", playersList);
		
		nbt.setTag("factionmod", compound);
	}

}
