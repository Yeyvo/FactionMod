package fr.mff.facmod.wand.util;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public abstract interface IWorldShim
{
  public abstract Block getBlock(Point3d paramPoint3d);
  
  public abstract boolean blockIsAir(Point3d paramPoint3d);
  
  public abstract World getWorld();
  
  public abstract boolean copyBlock(Point3d paramPoint3d1, Point3d paramPoint3d2);
  
  public abstract void setBlockToAir(Point3d paramPoint3d);
  
  public abstract int getMetadata(Point3d paramPoint3d);
  
  public abstract boolean entitiesInBox(AxisAlignedBB paramAxisAlignedBB);
  
  public abstract void playPlaceAtBlock(Point3d paramPoint3d, Block paramBlock);
  
  public abstract boolean setBlock(Point3d paramPoint3d, Block paramBlock, int paramInt);
}
