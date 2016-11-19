package fr.mff.facmod.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class FactionSaver extends WorldSavedData {

	private static FactionSaver INSTANCE;

	public static void save() {
		if(INSTANCE != null) {
			INSTANCE.markDirty();
		}
	}

	public FactionSaver(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound compound = nbt.getCompoundTag("factionMod");
		Faction.Registry.readFromNBT(compound.getCompoundTag("factionRegistry"));
		Lands.readfromNBT(compound.getCompoundTag("lands"));
		Homes.readFromNBT(compound.getCompoundTag("homes"));
		Powers.readFromNBT(compound.getCompoundTag("powers"));
		Rewards.readFromNBT(compound.getCompoundTag("reward"));

		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound compound = new NBTTagCompound();
		
		NBTTagCompound factionRegistry = new NBTTagCompound();
		Faction.Registry.writeToNBT(factionRegistry);
		compound.setTag("factionRegistry", factionRegistry);
		
		NBTTagCompound lands = new NBTTagCompound();
		Lands.writeToNBT(lands);
		compound.setTag("lands", lands);
		
		NBTTagCompound homes = new NBTTagCompound();
		Homes.writeToNBT(homes);
		compound.setTag("homes", homes);
		
		NBTTagCompound powers = new NBTTagCompound();
		Powers.writeToNBT(powers);
		compound.setTag("powers", powers);
		
		NBTTagCompound reward = new NBTTagCompound();
		Rewards.writeToNBT(reward);
		compound.setTag("rewards", reward);
		
		nbt.setTag("factionMod", compound);
	}

	public static void onServerStarting(FMLServerStartingEvent event) {
		if(!event.getServer().getEntityWorld().isRemote) {
			Faction.Registry.clear();
			Lands.clear();
			Homes.clearHomes();
			Powers.clear();
			MapStorage storage = event.getServer().getEntityWorld().getMapStorage();
			FactionSaver data = (FactionSaver)storage.loadData(FactionSaver.class, "factionMod");
			if(data == null) {
				data = new FactionSaver("factionMod");
				storage.setData("factionMod", data);
			}
			INSTANCE = data;
		}
	}

}
