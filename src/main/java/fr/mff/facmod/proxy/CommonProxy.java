package fr.mff.facmod.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import fr.mff.facmod.handlers.CommonEventHandler;

public class CommonProxy {

	protected static final Map<UUID, NBTTagCompound> extendedEntityData = new HashMap<UUID, NBTTagCompound>();

	public void preInit(FMLPreInitializationEvent event)
	{

	}

	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
	}

	public void registerItemTexture(Item item, int metadata, String name){}

	public void registerItemTexture(Item item, String name){}

	public void registerBlockTexture(Block block, int metadata, String name){}

	public void registerBlockTexture(Block block, String name){}


	public static void storeEntityData(UUID uuid, NBTTagCompound compound) {
		extendedEntityData.put(uuid, compound);
	}

	public static NBTTagCompound getEntityData(UUID uuid) {
		return extendedEntityData.remove(uuid);
	}



}
