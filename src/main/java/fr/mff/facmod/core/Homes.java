package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.config.ConfigFaction;

public class Homes {

	private static final HashMap<String, BlockPos> homes = new HashMap<String, BlockPos>();
	private static final HashMap<EntityPlayer, Object[]> tpTimers = new HashMap<EntityPlayer, Object[]>();

	public static void clear() {
		homes.clear();
	}

	public static HashMap<String, BlockPos> getHomes() {
		return homes;
	}

	public static EnumResult setHome(UUID uuid, BlockPos position) {
		Faction faction = Faction.Registry.getPlayerFaction(uuid);
		if(faction != null) {
			Member member = faction.getMember(uuid);
			if(member != null) {
				if(member.getRank().hasPermission(Permission.FACTION_HANDLING)) {
					ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(position).getChunkCoordIntPair();
					String factionName = Lands.getLandFaction().get(pair);

					if(factionName != null) {
						if(faction.getName().equalsIgnoreCase(factionName)) {
							BlockPos lastPos = homes.remove(factionName);
							if(lastPos != null) {
								MinecraftServer.getServer().getEntityWorld().setBlockState(lastPos.down(), Blocks.air.getDefaultState());
							}
							homes.put(factionName, position);
							MinecraftServer.getServer().getEntityWorld().setBlockState(position.down(), BlockRegistry.homeBase.getDefaultState());

							FactionSaver.save();
							return EnumResult.HOME_SET.clear().addInformation(EnumChatFormatting.WHITE.toString() + position.getX())
									.addInformation(EnumChatFormatting.WHITE.toString() + position.getY())
									.addInformation(EnumChatFormatting.WHITE.toString() + position.getZ());
						}
						return EnumResult.LAND_OF_THE_FACTION.clear().addInformation(EnumChatFormatting.GOLD + factionName);
					}
					return EnumResult.NOT_CLAIMED_LAND;
				}
				return EnumResult.NO_PERMISSION;
			}
			return EnumResult.ERROR;
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

	public static void writeToNBT(NBTTagCompound compound) {
		NBTTagList homesList = new NBTTagList();
		Iterator<Entry<String, BlockPos>> iterator = homes.entrySet().iterator();
		while(iterator.hasNext()) {
			NBTTagCompound homeTag = new NBTTagCompound();
			Entry<String, BlockPos> entry = iterator.next();
			homeTag.setString("faction", entry.getKey());
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
			homes.put(homeTag.getString("faction"), pos);
			FactionSaver.save();
		}
	}

	public static EnumResult goToHome(EntityPlayer player) {
		Faction faction = Faction.Registry.getPlayerFaction(player.getUniqueID());
		if(faction != null) {
			BlockPos pos = homes.get(faction.getName());
			if(pos != null) {
				tpTimers.put(player, new Object[]{player.getPosition(), 0});
				return EnumResult.TP_LAUNCHED.clear().addInformation(ConfigFaction.TP_DELAY);
			}
			return EnumResult.HOME_NOT_SET;
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

	public static void onLandUnclaimedPre(ChunkCoordIntPair pair) {
		String factionName = Lands.getLandFaction().get(pair);
		BlockPos pos = Homes.getHomes().get(factionName);
		if(pos != null) {
			ChunkCoordIntPair homeChunk = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(pos).getChunkCoordIntPair();
			if(homeChunk.equals(pair)) {
				MinecraftServer.getServer().getEntityWorld().setBlockToAir(pos.down());
				Homes.getHomes().remove(factionName);
			}
		}
	}

	public static void onWorldTick(TickEvent.WorldTickEvent event) {
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
							Faction faction = Faction.Registry.getPlayerFaction(entry.getKey().getUniqueID());
							if(faction != null) {
								BlockPos pos = homes.get(faction.getName());
								if(pos != null) {
									entry.getKey().setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
									remove.add(entry.getKey());
								} else {
									remove.add(entry.getKey());
									entry.getKey().addChatComponentMessage(new ChatComponentTranslation(EnumResult.HOME_NOT_SET.getLanguageKey(), new Object[0]));
								}
							} else {
								remove.add(entry.getKey());
								entry.getKey().addChatComponentMessage(new ChatComponentTranslation(EnumResult.NOT_IN_A_FACTION.getLanguageKey(), new Object[0]));
							}
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
	//commit de debug
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
