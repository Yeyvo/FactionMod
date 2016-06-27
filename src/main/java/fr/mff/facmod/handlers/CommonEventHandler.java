package fr.mff.facmod.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.mff.facmod.core.extendedProperties.ExtendedPropertieFaction;
import fr.mff.facmod.proxy.CommonProxy;

public class CommonEventHandler {

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && ExtendedPropertieFaction.get((EntityPlayer) event.entity) == null) {
			ExtendedPropertieFaction.register((EntityPlayer)event.entity);
		}
	}


	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			NBTTagCompound playerData = new NBTTagCompound();
			((ExtendedPropertieFaction)(event.entity.getExtendedProperties(ExtendedPropertieFaction.EXT_PROP_NAME))).saveNBTData(playerData);
			CommonProxy.storeEntityData(((EntityPlayer) event.entity).getUniqueID(), playerData);
			ExtendedPropertieFaction.saveProxyData((EntityPlayer) event.entity);
		}
	}


	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			NBTTagCompound playerData = CommonProxy.getEntityData(((EntityPlayer) event.entity).getUniqueID());
			if (playerData != null) {
				((ExtendedPropertieFaction)(event.entity.getExtendedProperties(ExtendedPropertieFaction.EXT_PROP_NAME))).loadNBTData(playerData);
			}
			((ExtendedPropertieFaction)(event.entity.getExtendedProperties(ExtendedPropertieFaction.EXT_PROP_NAME))).sync();
		}
	}





}
