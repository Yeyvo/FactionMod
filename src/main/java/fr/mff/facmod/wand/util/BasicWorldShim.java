package fr.mff.facmod.wand.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BasicWorldShim
  implements IWorldShim
{
  private World world;
  
  public BasicWorldShim(World world)
  {
    this.world = world;
  }
  
  public Block getBlock(Point3d point)
  {
    if (this.world != null) {
      return this.world.getBlockState(new BlockPos(point.x, point.y, point.z)).getBlock();
    }
    return null;
  }
  
  public boolean blockIsAir(Point3d point)
  {
    return this.world.isAirBlock(new BlockPos(point.x, point.y, point.z));
  }
  
  public World getWorld()
  {
    return this.world;
  }
  
  public boolean copyBlock(Point3d originalBlock, Point3d blockPos)
  {
    IBlockState blockState = this.world.getBlockState(new BlockPos(originalBlock.x, originalBlock.y, originalBlock.z));
    boolean retval = this.world.setBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z), blockState, 3);
    
    this.world.setBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z), blockState, 3);
    return retval;
  }
  
  public void setBlockToAir(Point3d blockPos)
  {
    this.world.setBlockToAir(new BlockPos(blockPos.x, blockPos.y, blockPos.z));
  }
  
  public int getMetadata(Point3d point)
  {
    if (this.world != null)
    {
      IBlockState state = this.world.getBlockState(new BlockPos(point.x, point.y, point.z));
      return state.getBlock().getMetaFromState(state);
    }
    return 0;
  }
  
  public boolean entitiesInBox(AxisAlignedBB box)
  {
    if (box == null) {
      return false;
    }
    List entitiesWithinAABB = this.world.getEntitiesWithinAABB(EntityLivingBase.class, box);
    return entitiesWithinAABB.size() > 0;
  }
  
  public void playPlaceAtBlock(Point3d position, Block blockType)
  {
    if ((position != null) && (blockType != null)) {
      this.world.playSoundEffect(position.x + 0.5D, position.y + 0.5D, position.z + 0.5D, blockType.stepSound.getPlaceSound(), (blockType.stepSound.getVolume() + 1.0F) / 2.0F, blockType.stepSound.getFrequency() * 0.8F);
    }
  }
  
  public boolean setBlock(Point3d position, Block block, int meta)
  {
    return this.world.setBlockState(position.toBlockPos(), block.getStateFromMeta(meta), 3);
  }
}
