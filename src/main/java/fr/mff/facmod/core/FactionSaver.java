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
		Homes.readFromNBT(compound.getCompoundTag("mobs"));

		Powers.readFromNBT(compound.getCompoundTag("powers"));
		
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
		
		NBTTagCompound mobs = new NBTTagCompound();
		Homes.writeToNBT(mobs);
		compound.setTag("mobs", mobs);
		
		NBTTagCompound powers = new NBTTagCompound();
		Powers.writeToNBT(powers);
		compound.setTag("powers", powers);
		
		nbt.setTag("factionMod", compound);
	}

	public static void onServerStarting(FMLServerStartingEvent event) {
		if(!event.getServer().getEntityWorld().isRemote) {
			Faction.Registry.clear();
			Lands.clear();
			Homes.clearHomes();
			Powers.clear();
			Homes.clearMob();
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
