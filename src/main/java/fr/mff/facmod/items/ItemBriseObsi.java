package fr.mff.facmod.items;

import fr.mff.facmod.FactionMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBriseObsi extends Item {

	public ItemBriseObsi() {
		super();
		this.setMaxDamage(2);
		this.setMaxStackSize(1);
		this.setCreativeTab(FactionMod.factionTabs);
		
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState block = worldIn.getBlockState(pos);
		if (block == Blocks.obsidian.getDefaultState()) {
			worldIn.setBlockToAir(pos);
			stack.damageItem(1, playerIn);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}
