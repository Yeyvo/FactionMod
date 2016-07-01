package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;

public class Lands {
	
	private static final HashMap<ChunkCoordIntPair, String> chunks = new HashMap<ChunkCoordIntPair, String>();
	private static final HashMap<UUID, String> playerCache = new HashMap<UUID, String>();
	
	public static void writeToNBT(NBTTagCompound compound) {
		Iterator<Entry<ChunkCoordIntPair, String>> iterator = Lands.chunks.entrySet().iterator();
		NBTTagList chunkList = new NBTTagList();
		while(iterator.hasNext()) {
			NBTTagCompound chunkTag = new NBTTagCompound();
			Entry<ChunkCoordIntPair, String> entry = iterator.next();
			chunkTag.setInteger("x", entry.getKey().chunkXPos);
			chunkTag.setInteger("z", entry.getKey().chunkZPos);
			chunkTag.setString("faction", entry.getValue());
			chunkList.appendTag(chunkTag);
		}
		compound.setTag("chunks", chunkList);
	}
	
	public static void readfromNBT(NBTTagCompound compound) {
		NBTTagList chunkList = (NBTTagList)compound.getTag("chunks");
		for(int i = 0; i < chunkList.tagCount(); i++) {
			NBTTagCompound chunkTag = chunkList.getCompoundTagAt(i);
			ChunkCoordIntPair coords = new ChunkCoordIntPair(chunkTag.getInteger("x"), chunkTag.getInteger("z"));
			chunks.put(coords, chunkTag.getString("faction"));
		}
	}
	
	public static HashMap<UUID, String> getPlayerCache() {
		return playerCache;
	}
	
	public static HashMap<ChunkCoordIntPair, String> getLandFaction() {
		return chunks;
	}
	
	public static void setPlayerCache(UUID uuid, String faction) {
		playerCache.put(uuid, faction);
	}
	
	public static void clearChunksFaction(String name) {
		List<ChunkCoordIntPair> toRemove = new ArrayList<ChunkCoordIntPair>();
		Iterator<Entry<ChunkCoordIntPair, String>> iterator = Lands.chunks.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<ChunkCoordIntPair, String> entry = iterator.next();
			if(entry.getValue().equalsIgnoreCase(name)) {
				toRemove.add(entry.getKey());
			}
		}
		for(ChunkCoordIntPair chunk : toRemove) {
			Lands.chunks.remove(chunk);
		}
		FactionSaver.save();
	}
	
	public static EnumResult claimChunk(UUID claimer, ChunkCoordIntPair pair) {
		Faction faction = Faction.Registry.getPlayerFaction(claimer);
		if(faction != null) {
			String name = chunks.get(pair);
			if(name == null) {
				chunks.put(pair, faction.getName());
				FactionSaver.save();
				return EnumResult.LAND_CLAIMED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
			}
			return EnumResult.ALREADY_CLAIMED_LAND.clear().addInformation(EnumChatFormatting.GOLD + name);
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

}
