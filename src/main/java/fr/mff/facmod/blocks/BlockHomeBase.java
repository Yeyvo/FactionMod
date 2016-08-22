package fr.mff.facmod.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.FactionSaver;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.tileentities.TileEntityHomeBase;

public class BlockHomeBase extends BlockContainer {

	public BlockHomeBase(Material material) {
		super(material);
		this.setHardness(1.0F);
		this.setResistance(1000.F);
		this.setLightLevel(1.0F);
		this.setCreativeTab(FactionMod.factionTabs);
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		String factionName = Lands.getLandFaction().get(world.getChunkFromBlockCoords(pos).getChunkCoordIntPair());
		Homes.getHomes().remove(factionName);
		FactionSaver.save();
		super.onBlockDestroyedByPlayer(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			player.openGui(FactionMod.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityHomeBase();
	}
	
	@Override
	public int getRenderType()
    {
        return 3;
    }

}
