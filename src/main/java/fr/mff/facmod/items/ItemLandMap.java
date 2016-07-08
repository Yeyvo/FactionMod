package fr.mff.facmod.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.network.PacketHelper;

public class ItemLandMap extends Item {
	
	public ItemLandMap() {
		this.setCreativeTab(FactionMod.factionTabs);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			PacketHelper.sendMap((EntityPlayerMP)player);
		}
		return stack;
	}
	
	

}
