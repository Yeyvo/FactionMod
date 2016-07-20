package fr.mff.facmod.core;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public abstract class Case {
	
	public abstract boolean isBreakAvalaible(World world, BlockPos pos, IBlockState state, EntityPlayer player);
	
	public abstract boolean isInteractAvalaible(World world, BlockPos pos, PlayerInteractEvent.Action action, @Nullable EnumFacing facing, @Nullable Vec3 lookPos);
	
	public abstract boolean isPlaceAvalaible(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack, IBlockState placedAgainst);
	
	public static enum Type {
		
		BREAK,
		PLACE,
		USE;
		
	}

}
