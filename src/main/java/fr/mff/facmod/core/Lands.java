package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.items.ItemRegistry;
import fr.mff.facmod.network.PacketHelper;

public class Lands {

	private static final HashMap<ChunkCoordIntPair, String> chunks = new HashMap<ChunkCoordIntPair, String>();
	private static final HashMap<UUID, String> playerCache = new HashMap<UUID, String>();

	public static void clear() {
		chunks.clear();
		playerCache.clear();
	}

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
		Lands.clear();
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

	public static void removePlayerCache(UUID uuid) {
		playerCache.remove(uuid);
	}

	public static void clearChunksFaction(String name) {
		List<ChunkCoordIntPair> toRemove = Lands.getLandsForFaction(name);
		for(ChunkCoordIntPair chunk : toRemove) {
			Lands.chunks.remove(chunk);
		}
		FactionSaver.save();
	}

	public static EnumResult claimChunk(UUID claimer, ChunkCoordIntPair pair) {
		Faction faction = Faction.Registry.getPlayerFaction(claimer);
		if(faction != null) {
			if(faction.getMember(claimer).getRank().hasPermission(Permission.LAND_HANDLING)) {
				String name = chunks.get(pair);
				if(name == null) {
					chunks.put(pair, faction.getName());
					FactionSaver.save();
					return EnumResult.LAND_CLAIMED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
				}
				return EnumResult.ALREADY_CLAIMED_LAND.clear().addInformation(EnumChatFormatting.GOLD + name);
			}
			return EnumResult.NO_PERMISSION;
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

	public static EnumResult unClaimChunk(UUID uuid, ChunkCoordIntPair pair) {
		Faction faction = Faction.Registry.getPlayerFaction(uuid);
		if(faction != null) {
			if(faction.getMember(uuid).getRank().hasPermission(Permission.LAND_HANDLING)) {
				String owner = chunks.get(pair);
				if(owner != null) {
					if(faction.getName().equalsIgnoreCase(owner)) {
						chunks.remove(pair);
						FactionSaver.save();
						return EnumResult.LAND_UNCLAIMED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
					}
					return EnumResult.CLAIMED_BY_FACTION.clear().addInformation(EnumChatFormatting.GOLD + owner);
				}
				return EnumResult.NOT_CLAIMED_LAND;
			}
			return EnumResult.NO_PERMISSION;
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(!event.player.getEntityWorld().isRemote) {
			ChunkCoordIntPair coords = event.player.getEntityWorld().getChunkFromBlockCoords(event.player.getPosition()).getChunkCoordIntPair();
			String factionName = Lands.getLandFaction().get(coords);
			String cacheFactionName = Lands.getPlayerCache().get(event.player.getUniqueID());
			if(factionName != cacheFactionName) {
				Lands.setPlayerCache(event.player.getUniqueID(), factionName);
				if(factionName != null) {
					if(!factionName.equalsIgnoreCase(cacheFactionName)) {
						Faction faction = Faction.Registry.getFactionFromName(factionName);
						event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[ " + factionName + (faction == null || faction.getDescription().equals("") ? "" : EnumChatFormatting.LIGHT_PURPLE + " - " + EnumChatFormatting.BLUE + faction.getDescription()) + EnumChatFormatting.GOLD + " ]"));
					}
				} else {
					event.player.addChatComponentMessage(new ChatComponentTranslation("faction.chunk.free", new Object[0]));
				}
				PacketHelper.updateLandOwner(event.player, factionName);
			}
		}
	}

	public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		if(event.player.worldObj != null && !event.player.worldObj.isRemote) {
			Lands.removePlayerCache(event.player.getUniqueID());
		}
	}

	public static List<ChunkCoordIntPair> getLandsForFaction(String factionName) {
		List<ChunkCoordIntPair> lands = new ArrayList<ChunkCoordIntPair>();
		Iterator<Entry<ChunkCoordIntPair, String>> iterator = Lands.chunks.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<ChunkCoordIntPair, String> entry = iterator.next();
			if(entry.getValue().equalsIgnoreCase(factionName)) {
				lands.add(entry.getKey());
			}
		}
		return lands;
	}

	/**
	 * Cancel if :
	 * <ul>
	 * 		<li>The land is claimed and the player isn't in a faction</li>
	 * 		<li>The land is claimed and the player is in an other faction</li>
	 * 		<li>The land is claimed, the player is in the faction and the player hasn't the permission to break blocks</li>
	 * </ul>
	 * @param event
	 */
	public static void onPlayerBreakBlock(BreakEvent event) {
		if(!event.world.isRemote && event.world == MinecraftServer.getServer().getEntityWorld()) {
			if(event.state.getBlock() == BlockRegistry.homeBase) {
				String factionName = Lands.getLandFaction().get(event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair());
				Homes.getHomes().remove(factionName);
				FactionSaver.save();
			} else {
				ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair();
				String ownerName = Lands.getLandFaction().get(coords);
				if(ownerName != null) {
					Faction faction = Faction.Registry.getPlayerFaction(event.getPlayer().getUniqueID());
					if(faction != null) {
						if(faction.getName().equalsIgnoreCase(ownerName)) {
							Member member = faction.getMember(event.getPlayer().getUniqueID());
							if(member != null) {
								if(!member.getRank().hasPermission(Permission.ALTER_BLOCK)) {
									event.setCanceled(true);
								}
							}
						} else {
							event.setCanceled(true);
						}
					} else {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	public static void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.world.isRemote && event.world == MinecraftServer.getServer().getEntityWorld()) {
			if(event.entityPlayer.getHeldItem() != null && (event.entityLiving.getHeldItem().getItem() == ItemRegistry.chestWatcher || event.entityPlayer.getHeldItem().getItem() == ItemRegistry.briseObsi) && event.entityPlayer.isSneaking()) {
				return;
			}
			ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair();
			String ownerName = Lands.getLandFaction().get(coords);
			if(ownerName != null) {
				Faction faction = Faction.Registry.getPlayerFaction(event.entityPlayer.getUniqueID());
				if(faction != null) {
					if(faction.getName().equalsIgnoreCase(ownerName)) {
						Member member = faction.getMember(event.entityPlayer.getUniqueID());
						if(member != null) {
							if(!member.getRank().hasPermission(Permission.USE_BLOCK)) {
								event.setCanceled(true);
							}
						}
					} else {
						event.setCanceled(true);
					}
				} else {
					event.setCanceled(true);
				}
			}
		}
	}

	public static void onPlayerPlaceBlock(PlaceEvent event) {
		if(!event.world.isRemote && event.world == MinecraftServer.getServer().getEntityWorld()) {
			ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair();
			String ownerName = Lands.getLandFaction().get(coords);
			if(ownerName != null) {
				Faction faction = Faction.Registry.getPlayerFaction(event.player.getUniqueID());
				if(faction != null) {
					if(faction.getName().equalsIgnoreCase(ownerName)) {
						Member member = faction.getMember(event.player.getUniqueID());
						if(member != null) {
							if(!member.getRank().hasPermission(Permission.ALTER_BLOCK)) {
								event.setCanceled(true);
							}
						}
					} else {
						event.setCanceled(true);
					}
				} else {
					event.setCanceled(true);
				}
			}
		}
	}

	public static void onBucketFill(FillBucketEvent event) {
		if(!event.world.isRemote && event.world == MinecraftServer.getServer().getEntityWorld()) {
			ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.target.getBlockPos()).getChunkCoordIntPair();
			String ownerName = Lands.getLandFaction().get(coords);
			if(ownerName != null) {
				Faction faction = Faction.Registry.getPlayerFaction(event.entityPlayer.getUniqueID());
				if(faction != null) {
					if(faction.getName().equalsIgnoreCase(ownerName)) {
						Member member = faction.getMember(event.entityPlayer.getUniqueID());
						if(member != null) {
							if(!member.getRank().hasPermission(Permission.ALTER_BLOCK)) {
								event.setCanceled(true);
							}
						}
					} else {
						event.setCanceled(true);
					}
				} else {
					event.setCanceled(true);
				}
			}
		}
	}
}
