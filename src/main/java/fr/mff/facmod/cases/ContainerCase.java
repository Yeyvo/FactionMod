package fr.mff.facmod.cases;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import fr.mff.facmod.core.Case;
import fr.mff.facmod.core.Lands;

public class ContainerCase extends Case {

	static {
		Lands.addCase(new ContainerCase(), Case.Type.USE);
	}

	@Override
	public boolean isBreakAvalaible(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean isInteractAvalaible(World world, BlockPos pos, Action action, EnumFacing facing, Vec3 lookPos) {
		Block block = world.getBlockState(pos).getBlock();
		if(block == Blocks.enchanting_table || block == Blocks.ender_chest || block == Blocks.crafting_table) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isPlaceAvalaible(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack, IBlockState placedAgainst) {
		return false;
	}

}
