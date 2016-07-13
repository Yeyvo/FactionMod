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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.google.common.collect.Lists;

import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.items.ItemRegistry;
import fr.mff.facmod.network.PacketHelper;

public class Lands {

	private static final HashMap<ChunkCoordIntPair, String> chunks = new HashMap<ChunkCoordIntPair, String>();
	private static final HashMap<UUID, String> playerCache = new HashMap<UUID, String>();
	private static final List<ChunkCoordIntPair> safeZones = Lists.newArrayList();
	private static final List<ChunkCoordIntPair> warZones = Lists.newArrayList();

	public static void clear() {
		chunks.clear();
		playerCache.clear();
		safeZones.clear();
		warZones.clear();
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

		NBTTagList safeZonesList = new NBTTagList();
		for(ChunkCoordIntPair chunk : safeZones) {
			NBTTagCompound safeZoneTag = new NBTTagCompound();
			safeZoneTag.setInteger("x", chunk.chunkXPos);
			safeZoneTag.setInteger("z", chunk.chunkZPos);
			safeZonesList.appendTag(safeZoneTag);
		}
		compound.setTag("safeZones", safeZonesList);

		NBTTagList warZoneList = new NBTTagList();
		for(ChunkCoordIntPair chunk : warZones) {
			NBTTagCompound warZoneTag = new NBTTagCompound();
			warZoneTag.setInteger("x", chunk.chunkXPos);
			warZoneTag.setInteger("z", chunk.chunkZPos);
			safeZonesList.appendTag(warZoneTag);
		}
		compound.setTag("warZones", warZoneList);
	}

	public static void readfromNBT(NBTTagCompound compound) {
		NBTTagList chunkList = (NBTTagList)compound.getTag("chunks");
		for(int i = 0; i < chunkList.tagCount(); i++) {
			NBTTagCompound chunkTag = chunkList.getCompoundTagAt(i);
			ChunkCoordIntPair coords = new ChunkCoordIntPair(chunkTag.getInteger("x"), chunkTag.getInteger("z"));
			chunks.put(coords, chunkTag.getString("faction"));
		}

		NBTTagList safeZonesList = (NBTTagList)compound.getTag("safeZones");
		for(int i = 0; i < safeZonesList.tagCount(); i++) {
			NBTTagCompound safeZoneTag = safeZonesList.getCompoundTagAt(i);
			ChunkCoordIntPair chunk = new ChunkCoordIntPair(safeZoneTag.getInteger("x"), safeZoneTag.getInteger("z"));
			safeZones.add(chunk);
		}

		NBTTagList warZonesList = (NBTTagList)compound.getTag("warZones");
		for(int i = 0; i < warZonesList.tagCount(); i++) {
			NBTTagCompound warZoneTag = warZonesList.getCompoundTagAt(i);
			ChunkCoordIntPair chunk = new ChunkCoordIntPair(warZoneTag.getInteger("x"), warZoneTag.getInteger("z"));
			warZones.add(chunk);
		}
	}

	public static HashMap<UUID, String> getPlayerCache() {
		return playerCache;
	}

	public static HashMap<ChunkCoordIntPair, String> getLandFaction() {
		return chunks;
	}

	public static List<ChunkCoordIntPair> getSafeZones() {
		return safeZones;
	}

	public static List<ChunkCoordIntPair> getWarZones() {
		return warZones;
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

	public static boolean isSafeZone(ChunkCoordIntPair coords) {
		for(ChunkCoordIntPair chunk : safeZones) {
			if(chunk.equals(coords)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isWarZone(ChunkCoordIntPair coords) {
		for(ChunkCoordIntPair chunk : warZones) {
			if(chunk.equals(coords)) {
				return true;
			}
		}
		return false;
	}

	public static EnumResult setWarZone(EntityPlayer player) {
		if(player.getEntityWorld().equals(MinecraftServer.getServer().getEntityWorld())) {
			ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
			if(Lands.isSafeZone(pair)) {
				return EnumResult.IN_A_SAFE_ZONE;
			}
			if(Lands.isWarZone(pair)) {
				return EnumResult.IN_A_WAR_ZONE;
			}
			String factionName = Lands.getLandFaction().get(pair);
			if(factionName != null) {
				return EnumResult.ALREADY_CLAIMED_LAND.clear().addInformation(factionName);
			} else {
				warZones.add(pair);
				FactionSaver.save();
				return EnumResult.WAR_ZONE_SET;
			}
		}
		return EnumResult.WRONG_WORLD;
	}

	public static EnumResult setSafeZone(EntityPlayer player) {
		if(player.getEntityWorld().equals(MinecraftServer.getServer().getEntityWorld())) {
			ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
			if(Lands.isSafeZone(pair)) {
				return EnumResult.IN_A_SAFE_ZONE;
			}
			if(Lands.isWarZone(pair)) {
				return EnumResult.IN_A_WAR_ZONE;
			}
			String factionName = Lands.getLandFaction().get(pair);
			if(factionName != null) {
				return EnumResult.ALREADY_CLAIMED_LAND.clear().addInformation(factionName);
			} else {
				safeZones.add(pair);
				FactionSaver.save();
				return EnumResult.SAFE_ZONE_SET;
			}
		}
		return EnumResult.WRONG_WORLD;
	}

	public static EnumResult removeZone(EntityPlayer player) {
		if(player.getEntityWorld().equals(MinecraftServer.getServer().getEntityWorld())) {
			ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(player.getPosition()).getChunkCoordIntPair();
			if(Lands.getSafeZones().remove(pair)) {
				FactionSaver.save();
				return EnumResult.REMOVED_SAFE_ZONE;
			}
			if(Lands.getWarZones().remove(pair)) {
				FactionSaver.save();
				return EnumResult.REMOVED_WAR_ZONE;
			}
			return EnumResult.NOT_WAR_OR_SAFE_ZONE;
		}
		return EnumResult.WRONG_WORLD;
	}

	public static EnumResult claimChunk(UUID claimer, ChunkCoordIntPair pair) {
		if(Lands.isSafeZone(pair)) {
			return EnumResult.IN_A_SAFE_ZONE;
		} else if(Lands.isWarZone(pair)) {
			return EnumResult.IN_A_WAR_ZONE;
		} else {
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
	}

	public static EnumResult unClaimChunk(UUID uuid, ChunkCoordIntPair pair) {
		if(Lands.isSafeZone(pair)) {
			return EnumResult.IN_A_SAFE_ZONE;
		} else if(Lands.isWarZone(pair)) {
			return EnumResult.IN_A_WAR_ZONE;
		} else {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction != null) {
				if(faction.getMember(uuid).getRank().hasPermission(Permission.LAND_HANDLING)) {
					String owner = chunks.get(pair);
					if(owner != null) {
						if(faction.getName().equalsIgnoreCase(owner)) {
							Homes.onLandUnclaimedPre(pair);
							chunks.remove(pair);
							FactionSaver.save();
							return EnumResult.LAND_UNCLAIMED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
						}
						return EnumResult.ALREADY_CLAIMED_LAND.clear().addInformation(EnumChatFormatting.GOLD + owner);
					}
					return EnumResult.NOT_CLAIMED_LAND;
				}
				return EnumResult.NO_PERMISSION;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}
	}

	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(!event.player.getEntityWorld().isRemote && event.player.getEntityWorld().equals(MinecraftServer.getServer().getEntityWorld())) {
			ChunkCoordIntPair coords = event.player.getEntityWorld().getChunkFromBlockCoords(event.player.getPosition()).getChunkCoordIntPair();
			String factionName = Lands.getLandFaction().get(coords);
			String cacheFactionName = Lands.getPlayerCache().get(event.player.getUniqueID());
			if(factionName == null) {
				if(Lands.isSafeZone(coords)) {
					factionName = "safezone";
				} else if(Lands.isWarZone(coords)) {
					factionName = "warzone";
				} else {
					factionName = "";
				}
			}
			if(!factionName.equalsIgnoreCase(cacheFactionName)) {
				Lands.setPlayerCache(event.player.getUniqueID(), factionName);
				if(!factionName.isEmpty() && !factionName.equalsIgnoreCase("safezone") && !factionName.equalsIgnoreCase("warzone")) {
					Faction faction = Faction.Registry.getFactionFromName(factionName);
					event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[ " + factionName + (faction == null || faction.getDescription().equals("") ? "" : EnumChatFormatting.LIGHT_PURPLE + " - " + EnumChatFormatting.BLUE + faction.getDescription()) + EnumChatFormatting.GOLD + " ]"));
				} else {
					if(Lands.isSafeZone(coords)) {
						event.player.addChatComponentMessage(new ChatComponentTranslation("faction.chunk.safeZone", new Object[0]));
					} else if(Lands.isWarZone(coords)) {
						event.player.addChatComponentMessage(new ChatComponentTranslation("faction.chunk.warZone", new Object[0]));
					} else {
						event.player.addChatComponentMessage(new ChatComponentTranslation("faction.chunk.free", new Object[0]));
					}
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
	 * 		<li>The land is a safe zone</li>
	 * 		<li>The land is a war zone</li>
	 * 		<li>The land is claimed and the player isn't in a faction</li>
	 * 		<li>The land is claimed and the player is in an other faction</li>
	 * 		<li>The land is claimed, the player is in the faction and the player hasn't the permission to break blocks</li>
	 * </ul>
	 * @param event
	 */
	public static void onPlayerBreakBlock(BreakEvent event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			if(event.state.getBlock() == BlockRegistry.homeBase) {
				String factionName = Lands.getLandFaction().get(event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair());
				Homes.getHomes().remove(factionName);
				FactionSaver.save();
			} else {
				ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair();
				if(Lands.isSafeZone(coords)) {
					event.getPlayer().addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_SAFE_ZONE.getLanguageKey(), new Object[0]));
					event.setCanceled(true);
				} else if(Lands.isWarZone(coords)) {
					event.getPlayer().addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_WAR_ZONE.getLanguageKey(), new Object[0]));
					event.setCanceled(true);
				} else {
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
	}

	public static void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			if(event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
				return;
			}
			ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair();
			if(Lands.isSafeZone(coords)) {
				event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_SAFE_ZONE.getLanguageKey(), new Object[0]));
				event.setCanceled(true);
			} else if(Lands.isWarZone(coords)) {
				event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_WAR_ZONE.getLanguageKey(), new Object[0]));
				event.setCanceled(true);
			} else {
				if(event.entityPlayer.getHeldItem() != null && (event.entityLiving.getHeldItem().getItem() == ItemRegistry.chestWatcher || event.entityPlayer.getHeldItem().getItem() == ItemRegistry.briseObsi) && event.entityPlayer.isSneaking()) {
					return;
				}
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
	}

	public static void onPlayerPlaceBlock(PlaceEvent event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.pos).getChunkCoordIntPair();
			if(Lands.isSafeZone(coords)) {
				event.player.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_SAFE_ZONE.getLanguageKey(), new Object[0]));
				event.setCanceled(true);
			} else if(Lands.isWarZone(coords)) {
				event.player.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_WAR_ZONE.getLanguageKey(), new Object[0]));
				event.setCanceled(true);
			} else {
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
	}

	public static void onBucketFill(FillBucketEvent event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			ChunkCoordIntPair coords = event.world.getChunkFromBlockCoords(event.target.getBlockPos()).getChunkCoordIntPair();
			if(Lands.isSafeZone(coords)) {
				event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_SAFE_ZONE.getLanguageKey(), new Object[0]));
				event.setCanceled(true);
			} else if(Lands.isWarZone(coords)) {
				event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_WAR_ZONE.getLanguageKey(), new Object[0]));
				event.setCanceled(true);
			} else {
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

	public static void onLivingAttack(LivingAttackEvent event) {
		if(!event.entityLiving.getEntityWorld().isRemote) {
			if(event.entityLiving.worldObj.equals(MinecraftServer.getServer().getEntityWorld())) {
				ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(event.entityLiving.getPosition()).getChunkCoordIntPair();
				if(Lands.isSafeZone(pair)) {
					event.setCanceled(true);
					event.entityLiving.addChatMessage(new ChatComponentTranslation(EnumResult.IN_A_SAFE_ZONE.getLanguageKey(), new Object[0]));
				}
			}
		}
	}

	public static void onAttackEntity(AttackEntityEvent event) {
		if(!event.entityLiving.getEntityWorld().isRemote) {
			if(event.entityLiving.worldObj.equals(MinecraftServer.getServer().getEntityWorld())) {
				ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(event.entityLiving.getPosition()).getChunkCoordIntPair();
				ChunkCoordIntPair pairTwo = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(event.target.getPosition()).getChunkCoordIntPair();
				if(Lands.isSafeZone(pair) || Lands.isSafeZone(pairTwo)) {
					event.setCanceled(true);
					event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation(EnumResult.IN_A_SAFE_ZONE.getLanguageKey(), new Object[0]));
				}
			}
		}
	}

	public static void onLivingHurt(LivingHurtEvent event) {
		if(!event.entityLiving.getEntityWorld().isRemote) {
			if(event.entityLiving.worldObj.equals(MinecraftServer.getServer().getEntityWorld())) {
				ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(event.entityLiving.getPosition()).getChunkCoordIntPair();
				if(Lands.isSafeZone(pair)) {
					event.setCanceled(true);
				}
			}
		}
	}

	public static void onExplosion(ExplosionEvent.Detonate event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			List<BlockPos> dontExplode = Lists.newArrayList();
			for(BlockPos pos : event.getAffectedBlocks()) {
				ChunkCoordIntPair pair = event.world.getChunkFromBlockCoords(pos).getChunkCoordIntPair();
				if(Lands.isSafeZone(pair) || Lands.isWarZone(pair)) {
					dontExplode.add(pos);
				}
			}
			for(BlockPos pos : dontExplode) {
				event.getAffectedBlocks().remove(pos);
			}
		}
	}
}