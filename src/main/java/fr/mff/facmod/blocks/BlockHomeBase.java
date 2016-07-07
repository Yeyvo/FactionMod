package fr.mff.facmod.blocks;

import java.util.Random;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.tileentities.TileEntityHomeBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockHomeBase extends BlockContainer {

	public BlockHomeBase(Material material) {
		super(material);
		this.setHardness(1.0F);
		this.setResistance(1000.F);
		this.setLightLevel(1.0F);
		this.setCreativeTab(FactionMod.factionTabs);
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	protected boolean canSilkHarvest() {
		return false;
	}

	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityHomeBase();
	}
	
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile instanceof TileEntityHomeBase)
		{
			InventoryHelper.dropInventoryItems(world, pos, (TileEntityHomeBase) tile);
		}
		super.breakBlock(world, pos, state);
	}
}
