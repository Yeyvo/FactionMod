package fr.mff.facmod.core.extendedProperties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.features.Faction;
import fr.mff.facmod.proxy.CommonProxy;
import fr.mff.facmod.tileentities.network.PacketSetFaction;

public class ExtendedPropertieFaction implements IExtendedEntityProperties {

	public static final String EXT_PROP_NAME = "ExtPropFaction";

	private final EntityPlayer player;
	public String factionName;

	public ExtendedPropertieFaction(EntityPlayer player) {
		this.player = player;
		this.factionName = "";
	}
	
	public void setFaction(Faction faction) {
		this.factionName = faction.getName();
		this.sync();
	}
	
	public void setNoFaction() {
		this.factionName = "";
		this.sync();
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(EXT_PROP_NAME, new ExtendedPropertieFaction(player));
	}

	public static final ExtendedPropertieFaction get(EntityPlayer player) {
		return (ExtendedPropertieFaction)player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("facName", factionName);
		compound.setTag(EXT_PROP_NAME, c);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound c = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		this.factionName = c.getString("facName");
	}

	public final void sync() {
		PacketSetFaction packet = new PacketSetFaction(factionName);
		FactionMod.network.sendToServer(packet);
		
		if(!player.getEntityWorld().isRemote) {
			FactionMod.network.sendTo(packet, (EntityPlayerMP)player);
		}
	}	

	public static void saveProxyData(EntityPlayer player) {
		ExtendedPropertieFaction playerData = ExtendedPropertieFaction.get(player);
		NBTTagCompound savedData = new NBTTagCompound();

		playerData.saveNBTData(savedData);
		CommonProxy.storeEntityData(player.getUniqueID(), savedData);
	}

	public static void loadProxyData(EntityPlayer player) {
		ExtendedPropertieFaction playerData = ExtendedPropertieFaction.get(player);
		NBTTagCompound savedData = CommonProxy.getEntityData(player.getUniqueID());

		if (savedData != null) {
			playerData.loadNBTData(savedData);
		}
		playerData.sync();
	}
	
	@Override
	public void init(Entity entity, World world) {

	}

}
