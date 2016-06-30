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
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldSavedData;
import fr.mff.facmod.core.features.EnumRank;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;
import fr.mff.facmod.network.PacketHelper;

public class SystemHandler extends WorldSavedData {

	private static SystemHandler INSTANCE;
	private final List<Faction> factions = new ArrayList<Faction>();
	private final HashMap<UUID, String> players = new HashMap<UUID, String>();
	private final HashMap<ChunkCoordIntPair, String> lands = new HashMap<ChunkCoordIntPair, String>();
	private final HashMap<UUID, String> playerLandCache = new HashMap<UUID, String>();

	public static List<Faction> getAllFactions() {
		return INSTANCE.factions;
	}
	
	public static HashMap<UUID, String> getPlayers() {
		return INSTANCE.players;
	}
	
	public static HashMap<ChunkCoordIntPair, String> getLands() {
		return INSTANCE.lands;
	}
	
	public static HashMap<UUID, String> getPlayerLandCache() {
		return INSTANCE.playerLandCache;
	}
	
	/**
	 * Associate a land to a faction
	 * @param coords
	 * @param faction
	 */
	public static void setLandProprietary(ChunkCoordIntPair coords, Faction faction) {
		INSTANCE.lands.put(coords, faction.getName());
		INSTANCE.markDirty();
	}
	
	/**
	 * Dissociate a land from any faction
	 * @param coords
	 */
	public static void removeLand(ChunkCoordIntPair coords) {
		INSTANCE.lands.remove(coords);
	}
	
	/**
	 * Associate player to a faction's name
	 * <ul>
	 * 		<li>Put UUID and faction's name to the map</li>
	 * 		<li>Update the client</li>
	 * </ul<
	 * @param uuid
	 * @param factionName
	 */
	public static void setPlayer(UUID uuid, String factionName) {
		INSTANCE.players.put(uuid, factionName);
		PacketHelper.updateClient(uuid);
		INSTANCE.markDirty();
	}

	/**
	 * Remove a faction from the list
	 * @param faction
	 * @return
	 */
	public static boolean removeFaction(Faction faction) {
		INSTANCE.markDirty();
		List<ChunkCoordIntPair> toRemove = new ArrayList<ChunkCoordIntPair>();
		Iterator<Entry<ChunkCoordIntPair, String>> iterator = INSTANCE.lands.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<ChunkCoordIntPair, String> entry = iterator.next();
			if(entry.getValue().equalsIgnoreCase(faction.getName())) {
				toRemove.add(entry.getKey());
			}
		}
		for(ChunkCoordIntPair coord : toRemove) {
			SystemHandler.removeLand(coord);
		}
		return INSTANCE.factions.remove(faction);
	}

	/**
	 * Add a faction to the list
	 * @param faction
	 * @return
	 */
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
		
		NBTTagList landsList = (NBTTagList)compound.getTag("lands");
		for(int i = 0; i < landsList.tagCount(); i++) {
			NBTTagCompound landTag = landsList.getCompoundTagAt(i);
			ChunkCoordIntPair coords = new ChunkCoordIntPair(landTag.getInteger("x"), landTag.getInteger("z"));
			this.lands.put(coords, landTag.getString("faction"));
			
		}
		
		NBTTagList playersList = (NBTTagList)compound.getTag("players");
		for(int i = 0; i < playersList.tagCount(); i++) {
			NBTTagCompound playerTag = playersList.getCompoundTagAt(i);
			//SystemHandler.setPlayer(UUID.fromString(playerTag.getString("uuid")), playerTag.getString("faction"));
			this.players.put(UUID.fromString(playerTag.getString("uuid")), playerTag.getString("faction"));
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
		
		NBTTagList landsList = new NBTTagList();
		Iterator<Entry<ChunkCoordIntPair, String>> iterator2 = this.lands.entrySet().iterator();
		while(iterator2.hasNext()) {
			NBTTagCompound landTag = new NBTTagCompound();
			Entry<ChunkCoordIntPair, String> entry = iterator2.next();
			landTag.setInteger("x", entry.getKey().chunkXPos);
			landTag.setInteger("z", entry.getKey().chunkZPos);
			landTag.setString("faction", entry.getValue());
			
			landsList.appendTag(landTag);
		}
		
		compound.setTag("factions", factionsList);
		compound.setTag("players", playersList);
		compound.setTag("lands", landsList);
		
		nbt.setTag("factionmod", compound);
	}

}
