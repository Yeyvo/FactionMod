		package fr.mff.facmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.FactionSaver;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;

public class BlockHomeBase extends Block{

	public BlockHomeBase(Material material) {
		super(material);
		this.setHardness(1.0F);
		this.setResistance(1000.F);
		this.setLightLevel(1.0F);
		this.setCreativeTab(FactionMod.factionTabs);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(world, pos, state);
		String factionName = Lands.getLandFaction().get(world.getChunkFromBlockCoords(pos).getChunkCoordIntPair());
		Homes.getHomes().remove(factionName);
		FactionSaver.save();
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	protected boolean canSilkHarvest() {
		return false;
	}

}
