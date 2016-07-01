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

	public static void onServerStarting(FMLServerStartingEvent event) {
		if(!event.getServer().getEntityWorld().isRemote) {
			Faction.Registry.clear();
			Lands.clear();
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
