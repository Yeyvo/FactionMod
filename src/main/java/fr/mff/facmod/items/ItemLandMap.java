package fr.mff.facmod.items;

import fr.mff.facmod.FactionMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLandMap extends Item {
	
	public ItemLandMap() {
		this.setCreativeTab(FactionMod.factionTabs);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer playerIn) {
		return stack;
	}
	
	

}
