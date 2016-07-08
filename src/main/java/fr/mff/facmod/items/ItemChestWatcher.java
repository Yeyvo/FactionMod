package fr.mff.facmod.items;

import fr.mff.facmod.FactionMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemChestWatcher extends Item{

	public ItemChestWatcher()
	{
		this.setMaxStackSize(1);
		this.setMaxDamage(5);
		this.setCreativeTab(FactionMod.factionTabs);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile == null)
			{
				return false;
			}
			if (tile instanceof IInventory)
			{
				player.openGui(FactionMod.INSTANCE, 1, world, pos.getX(), pos.getY(), pos.getZ());
				
				return true;
			}
		}
		
		return true;
	}
}
