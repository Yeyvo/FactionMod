package fr.mff.facmod.cases;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.core.Case;
import fr.mff.facmod.core.Lands;

public class HomeCase extends Case {
	
	static {
		Lands.addCase(new HomeCase(), Case.Type.BREAK);
	}

	@Override
	public boolean isBreakAvalaible(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(state.getBlock() == BlockRegistry.homeBase) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isInteractAvalaible(World world, BlockPos pos, Action action, EnumFacing facing, Vec3 lookPos) {
		return false;
	}

	@Override
	public boolean isPlaceAvalaible(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack, IBlockState placedAgainst) {
		return false;
	}
}
