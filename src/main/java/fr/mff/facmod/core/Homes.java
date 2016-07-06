package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import fr.mff.facmod.config.ConfigFaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Homes {

	private static final HashMap<UUID, BlockPos> homes = new HashMap<UUID, BlockPos>();
	private static final HashMap<EntityPlayer, Object[]> tpTimers = new HashMap<EntityPlayer, Object[]>();
	
	public static void clear() {
		homes.clear();
	}

	public static HashMap<UUID, BlockPos> getHomes() {
		return homes;
	}

	public static EnumResult setHome(UUID uuid, BlockPos position) {
		Faction faction = Faction.Registry.getPlayerFaction(uuid);
		if(faction != null) {
			ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(position).getChunkCoordIntPair();
			String factionName = Lands.getLandFaction().get(pair);
			if(factionName == null || faction.getName().equalsIgnoreCase(factionName)) {
				homes.remove(uuid);
				homes.put(uuid, position);
				return EnumResult.HOME_SET.clear().addInformation(EnumChatFormatting.WHITE.toString() + position.getX())
						.addInformation(EnumChatFormatting.WHITE.toString() + position.getY())
						.addInformation(EnumChatFormatting.WHITE.toString() + position.getZ());
			}
			return EnumResult.LAND_OF_THE_FACTION.clear().addInformation(EnumChatFormatting.GOLD + factionName);
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

	public static void writeToNBT(NBTTagCompound compound) {
		NBTTagList homesList = new NBTTagList();
		Iterator<Entry<UUID, BlockPos>> iterator = homes.entrySet().iterator();
		while(iterator.hasNext()) {
			NBTTagCompound homeTag = new NBTTagCompound();
			Entry<UUID, BlockPos> entry = iterator.next();
			homeTag.setString("uuid", entry.getKey().toString());
			homeTag.setInteger("x", entry.getValue().getX());
			homeTag.setInteger("y", entry.getValue().getY());
			homeTag.setInteger("z", entry.getValue().getZ());
			homesList.appendTag(homeTag);
		}
		compound.setTag("homes", homesList);
	}

	public static void readFromNBT(NBTTagCompound compound) {
		Homes.clear();
		NBTTagList homesList = (NBTTagList)compound.getTag("homes");
		for(int i = 0; i < homesList.tagCount(); i++) {
			NBTTagCompound homeTag = homesList.getCompoundTagAt(i);
			BlockPos pos = new BlockPos(homeTag.getInteger("x"), homeTag.getInteger("y"), homeTag.getInteger("z"));
			homes.put(UUID.fromString(homeTag.getString("uuid")), pos);
			FactionSaver.save();
		}
	}

	public static void onLandClaimed(ChunkCoordIntPair pair, String facName) {
		List<UUID> toRemove = new ArrayList<UUID>();
		Iterator<Entry<UUID, BlockPos>> iterator = homes.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<UUID, BlockPos> entry = iterator.next();
			Faction playerFaction = Faction.Registry.getPlayerFaction(entry.getKey());
			if(playerFaction == null || !playerFaction.getName().equalsIgnoreCase(facName)) {
				toRemove.add(entry.getKey());
			}
		}
		for(UUID uuid : toRemove) {
			homes.remove(uuid);
			Entity entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
			if(entity instanceof EntityPlayer) {
				entity.addChatMessage(new ChatComponentTranslation("msg.homeRemoved", new Object[0]));
			}
		}
		FactionSaver.save();
	}

	public static EnumResult goToHome(EntityPlayer player) {
		BlockPos pos = homes.get(player.getUniqueID());
		if(pos != null) {
			tpTimers.put(player, new Object[]{player.getPosition(), 0});
			return EnumResult.TP_LANUCHED.clear().addInformation(EnumChatFormatting.WHITE.toString() + ConfigFaction.TP_DELAY);
		}
		return EnumResult.HOME_NOT_SET;
	}

	public static void onPlayerTick(TickEvent.WorldTickEvent event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			List<EntityPlayer> remove = new ArrayList<EntityPlayer>();

			Iterator<Entry<EntityPlayer, Object[]>> iterator = tpTimers.entrySet().iterator();
			if(iterator.hasNext()) {
				Entry<EntityPlayer, Object[]> entry = iterator.next();

				if(entry.getKey().getPosition().equals(entry.getValue()[0])) {
					Integer tick = (Integer) entry.getValue()[1];
					tick += 1;
					entry.getValue()[1] = tick;
					if(tick % 20 == 0) {
						if(tick / 20 >= ConfigFaction.TP_DELAY) {
							BlockPos pos = homes.get(entry.getKey().getUniqueID());
							entry.getKey().setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
							remove.add(entry.getKey());
						} else {
							entry.getKey().addChatComponentMessage(new ChatComponentTranslation("msg.tpTimer", String.valueOf(ConfigFaction.TP_DELAY - tick / 20)));
						}
					}
				} else {
					remove.add(entry.getKey());
					entry.getKey().addChatComponentMessage(new ChatComponentTranslation("msg.tpCanceled", new Object[0]));
				}
			}

			for(EntityPlayer player : remove) {
				tpTimers.remove(player);
			}
		}
	}

	public static void onLivingHurt(LivingHurtEvent event) {
		if(!event.entity.worldObj.isRemote) {
			if(event.entity instanceof EntityPlayer) {
				Object o = tpTimers.remove(event.entity);
				if(o != null) {
					((EntityPlayer)event.entity).addChatComponentMessage(new ChatComponentTranslation("msg.tpCanceled", new Object[0]));
				}
			}
		}
	}

}
