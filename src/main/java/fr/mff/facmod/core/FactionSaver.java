package fr.mff.facmod.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

public class FactionSaver extends WorldSavedData {

	private static FactionSaver INSTANCE;
	private static boolean loaded = false;

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
		nbt.setTag("factionMod", compound);
	}

	public static void onWorldLoad(WorldEvent.Load event) {
		//TODO Fix the bug : all maps have same datas
		if(!event.world.isRemote && !loaded) {
			MapStorage storage = event.world.getMapStorage();
			FactionSaver data = (FactionSaver)storage.loadData(FactionSaver.class, "factionMod");
			if(data == null) {
				data = new FactionSaver("factionMod");
				storage.setData("factionMod", data);
			}
			INSTANCE = data;
			loaded = true;
		}
	}

}
