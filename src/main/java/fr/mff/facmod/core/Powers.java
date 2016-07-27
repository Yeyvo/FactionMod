package fr.mff.facmod.core;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.google.common.collect.Maps;

import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.network.PacketHelper;

public class Powers {

	private static final HashMap<UUID, Integer> powers = Maps.newHashMap();
	private static final HashMap<UUID, Integer> timers = Maps.newHashMap();

	public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
		if(!event.player.getEntityWorld().isRemote) {
			resetTimer(event.player.getUniqueID());
			if(powers.get(event.player.getUniqueID()) == null) {
				powers.put(event.player.getUniqueID(), ConfigFaction.POWER_PER_PLAYER);
				FactionSaver.save();
			}
		}
	}

	public static void onLivingDeath(LivingDeathEvent event) {
		if(!event.entity.getEntityWorld().isRemote) {
			if(event.entity instanceof EntityPlayer) {
				int playerPower = powers.get(event.entity.getUniqueID());
				playerPower = MathHelper.clamp_int(playerPower - ConfigFaction.POWER_LOST_ON_DEATH, 0, ConfigFaction.POWER_PER_PLAYER);
				powers.put(event.entity.getUniqueID(), playerPower);
				PacketHelper.updatePowerLevel(event.entity.getUniqueID());
				FactionSaver.save();
			}
		}
	}

	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(!event.player.getEntityWorld().isRemote) {
			Integer timer = timers.get(event.player.getUniqueID());
			timer += 1;
			timers.put(event.player.getUniqueID(), timer);
			if(timer % 20 == 0) {
				if(timer / 20 >= ConfigFaction.POWER_GIVING_FREQUENCY * 60) {
					int playerPower = getPowerOf(event.player.getUniqueID());
					playerPower = MathHelper.clamp_int(playerPower + ConfigFaction.POWER_GIVEN_FREQUENTLY, 0, ConfigFaction.POWER_PER_PLAYER);
					setPlayerPower(event.player.getUniqueID(), playerPower);
					timers.put(event.player.getUniqueID(), 0);
					PacketHelper.updatePowerLevel(event.player.getUniqueID());
					FactionSaver.save();
				}
			}
		}
	}

	public static void setPlayerPower(UUID uuid, int power) {
		powers.put(uuid, power);
	}

	public static void resetTimer(UUID uuid) {
		timers.put(uuid, 0);
	}

	public static int getPowerOf(UUID uuid) {
		return powers.get(uuid) == null ? 0 : powers.get(uuid).intValue();
	}

	public static long getTimerOf(UUID uuid) {
		return timers.get(uuid) == null ? 0 : timers.get(uuid).intValue();
	}

	public static void clear() {
		powers.clear();
		timers.clear();
	}

	public static void writeToNBT(NBTTagCompound compound) {
		NBTTagList powersList = new NBTTagList();
		for(Entry<UUID, Integer> entry : powers.entrySet()) {
			NBTTagCompound powerTag = new NBTTagCompound();
			powerTag.setString("uuid", entry.getKey().toString());
			powerTag.setInteger("power", entry.getValue());
			powersList.appendTag(powerTag);
		}
		compound.setTag("powers", powersList);
	}

	public static void readFromNBT(NBTTagCompound compound) {
		NBTTagList powersList = (NBTTagList)compound.getTag("powers");
		for(int i = 0; i < powersList.tagCount(); i++) {
			NBTTagCompound powerTag = powersList.getCompoundTagAt(i);
			powers.put(UUID.fromString(powerTag.getString("uuid")), powerTag.getInteger("power"));
		}
	}

}
