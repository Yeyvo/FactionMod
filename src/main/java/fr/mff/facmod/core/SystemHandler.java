package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldSavedData;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.core.features.Member;
import fr.mff.facmod.network.PacketHelper;

public class SystemHandler extends WorldSavedData {

	private static SystemHandler INSTANCE = new SystemHandler("factionmodsave");
	private final List<Faction> factions = new ArrayList<Faction>();
	private final HashMap<UUID, String> players = new HashMap<UUID, String>();

	public static List<Faction> getAllFactions() {
		return INSTANCE.factions;
	}
	
	public static HashMap<UUID, String> getPlayers() {
		return INSTANCE.players;
	}
	
	public static void setPlayer(EntityPlayer player, String factionName) {
		INSTANCE.players.put(player.getUniqueID(), factionName);
		PacketHelper.updateClient(player);
	}
	
	public static void removePlayer(EntityPlayer player) {
		INSTANCE.players.remove(player.getUniqueID());
	}

	public static boolean removeFaction(Faction faction) {
		return INSTANCE.factions.remove(faction);
	}

	public static boolean addFaction(Faction faction) {
		return INSTANCE.factions.add(faction);
	}
	
	//--------------------Instance---------------------------------------------------//
	
	private SystemHandler(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
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
				member.getPlayer().writeEntityToNBT(memberTag);
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
