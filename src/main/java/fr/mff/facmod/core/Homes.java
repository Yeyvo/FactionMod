package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.sun.scenario.effect.impl.state.GaussianRenderState;

import java.util.UUID;

import fr.mff.facmod.achievements.AchievementRegistry;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.entity.EntityFactionGuardian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Homes {

	private static final HashMap<String, BlockPos> homes = new HashMap<String, BlockPos>();
	private static final HashMap<String, EntityFactionGuardian> mobMap = new HashMap<String, EntityFactionGuardian>();

	private static final HashMap<EntityPlayer, Object[]> tpTimers = new HashMap<EntityPlayer, Object[]>();

	public static void clearHomes() {
		homes.clear();
		}
	public static void clearMob(){
		mobMap.clear();
	}

	public static HashMap<String, BlockPos> getHomes() {
		return homes;
	}

	public static EnumResult setHome(EntityPlayer player, BlockPos position) {
		UUID uuid = player.getUniqueID();
		Faction faction = Faction.Registry.getPlayerFaction(uuid);
		if (faction != null) {
			Member member = faction.getMember(uuid);
			if (member != null) {
				if (member.getRank().hasPermission(Permission.FACTION_HANDLING)) {
					ChunkCoordIntPair pair = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(position).getChunkCoordIntPair();
					String factionName = Lands.getLandFaction().get(pair);
					if (factionName != null) {
						if (faction.getName().equalsIgnoreCase(factionName)) {
							EntityFactionGuardian gardian = new EntityFactionGuardian(player.getEntityWorld());
							IBlockState state = MinecraftServer.getServer().getEntityWorld().getBlockState(position.down());
							if (state.getBlock() != Blocks.bedrock && state.getBlock() != BlockRegistry.homeBase) {
								BlockPos lastPos = homes.remove(factionName);
								EntityFactionGuardian lastmob = mobMap.get(factionName);
								if (lastPos != null) {
									MinecraftServer.getServer().getEntityWorld().setBlockState(lastPos.down(), Blocks.air.getDefaultState());
									lastmob.sync();
									lastmob.setDead();
								}
								homes.put(factionName, position);
								mobMap.put(factionName, gardian);
								state.getBlock().onBlockDestroyedByPlayer(MinecraftServer.getServer().getEntityWorld(), position.down(), state);
								MinecraftServer.getServer().getEntityWorld().setBlockState(position.down(), BlockRegistry.homeBase.getDefaultState());
								FactionSaver.save();
								gardian.setLocationAndAngles(position.getX() + 0.5D, position.getY() + 2, position.getZ() + 0.5D, 0.0F, 0.0F);
								gardian.setOwner(player);
								gardian.setGuardianName(faction.getName());
								player.getEntityWorld().spawnEntityInWorld(gardian);
								player.triggerAchievement(AchievementRegistry.homeSet);
								return EnumResult.HOME_SET.clear().addInformation(EnumChatFormatting.WHITE.toString() + position.getX()).addInformation(EnumChatFormatting.WHITE.toString() + position.getY()).addInformation(EnumChatFormatting.WHITE.toString() + position.getZ());
							}
							return EnumResult.NO_PERMISSION;
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
		NBTTagList mobList = new NBTTagList();

		Iterator<Entry<String, BlockPos>> iterator = homes.entrySet().iterator();
		while (iterator.hasNext()) {
			NBTTagCompound homeTag = new NBTTagCompound();
			Entry<String, BlockPos> entry = iterator.next();
			homeTag.setString("faction", entry.getKey());
			homeTag.setInteger("x", entry.getValue().getX());
			homeTag.setInteger("y", entry.getValue().getY());
			homeTag.setInteger("z", entry.getValue().getZ());
			homesList.appendTag(homeTag);
		}

		compound.setTag("homes", homesList);

		Iterator<Entry<String, EntityFactionGuardian>> iterator2 = mobMap.entrySet().iterator();
		while (iterator2.hasNext()) {
			NBTTagCompound mobTag = new NBTTagCompound();
			Entry<String, EntityFactionGuardian> entry2 = iterator2.next();
			mobTag.setString("faction", entry2.getKey());
			mobTag.setString("id", entry2.getValue().getUniqueID().toString()
			mobList.appendTag(mobTag);
			System.out.println(mobTag);


		}
		compound.setTag("mobs", mobList);
	}

	public static void readFromNBT(NBTTagCompound compound) {
		Homes.clearHomes();
		Homes.clearMob();

		NBTTagList homesList = (NBTTagList) compound.getTag("homes");
		NBTTagList mobList = (NBTTagList) compound.getTag("mobs");

		for (int i = 0; i < homesList.tagCount(); i++) {
			NBTTagCompound homeTag = homesList.getCompoundTagAt(i);
			BlockPos pos = new BlockPos(homeTag.getInteger("x"), homeTag.getInteger("y"), homeTag.getInteger("z"));
			homes.put(homeTag.getString("faction"), pos);
			FactionSaver.save();
		}
		for (int i = 0; i < mobList.tagCount(); i++) {
			NBTTagCompound mobTag = mobList.getCompoundTagAt(i);
			World world = DimensionManager.getWorld(0);
			EntityFactionGuardian entity = new EntityFactionGuardian(world);
			
			mobMap.put(mobTag.getString("faction"), entity);
			FactionSaver.save();
			System.out.println(mobMap.get(entity.getName()));
		}
	}


	public static EnumResult goToHome(EntityPlayer player) {
		Faction faction = Faction.Registry.getPlayerFaction(player.getUniqueID());
		if (faction != null) {
			BlockPos pos = homes.get(faction.getName());
			if (pos != null) {
				tpTimers.put(player, new Object[] { player.getPosition(), 0 });
				return EnumResult.TP_LAUNCHED.clear().addInformation(ConfigFaction.TP_DELAY);
			}
			return EnumResult.HOME_NOT_SET;
		}
		return EnumResult.NOT_IN_A_FACTION;
	}

	public static void onLandUnclaimedPre(ChunkCoordIntPair pair) {
		String factionName = Lands.getLandFaction().get(pair);
		BlockPos pos = Homes.getHomes().get(factionName);
		EntityFactionGuardian lastmob = mobMap.get(factionName);
		if (pos != null) {
			ChunkCoordIntPair homeChunk = MinecraftServer.getServer().getEntityWorld().getChunkFromBlockCoords(pos).getChunkCoordIntPair();
			if (homeChunk.equals(pair)) {
				MinecraftServer.getServer().getEntityWorld().setBlockToAir(pos.down());
				Homes.getHomes().remove(factionName);
				lastmob.sync();
				lastmob.setDead();
			}
		}
	}

	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		if (!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			List<EntityPlayer> remove = new ArrayList<EntityPlayer>();

			Iterator<Entry<EntityPlayer, Object[]>> iterator = tpTimers.entrySet().iterator();
			if (iterator.hasNext()) {
				Entry<EntityPlayer, Object[]> entry = iterator.next();

				if (entry.getKey().getPosition().equals(entry.getValue()[0])) {
					Integer tick = (Integer) entry.getValue()[1];
					tick += 1;
					entry.getValue()[1] = tick;
					if (tick % 20 == 0) {
						if (tick / 20 >= ConfigFaction.TP_DELAY) {
							Faction faction = Faction.Registry.getPlayerFaction(entry.getKey().getUniqueID());
							if (faction != null) {
								BlockPos pos = homes.get(faction.getName());
								if (pos != null) {
									entry.getKey().setPositionAndUpdate(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
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

			for (EntityPlayer player : remove) {
				tpTimers.remove(player);
			}
		}
	}

	public static void onLivingHurt(LivingHurtEvent event) {
		if (!event.entity.worldObj.isRemote) {
			if (event.entity instanceof EntityPlayer) {
				Object o = tpTimers.remove(event.entity);
				if (o != null) {
					((EntityPlayer) event.entity).addChatComponentMessage(new ChatComponentTranslation("msg.tpCanceled", new Object[0]));
				}
			}
		}
	}

}
