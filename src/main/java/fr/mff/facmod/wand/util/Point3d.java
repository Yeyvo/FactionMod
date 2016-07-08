package fr.mff.facmod.wand.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Point3d
{
  public static int UP = 1;
  public static int DOWN = -1;
  public static int EAST = 1;
  public static int WEST = -1;
  public static int SOUTH = 1;
  public static int NORTH = -1;
  public int x;
  public int y;
  public int z;
  
  public Point3d(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Point3d move(EnumFacing direction)
  {
    int newX = this.x;int newY = this.y;int newZ = this.z;
    switch (direction)
    {
    case UP: 
      newY++;
      break;
    case DOWN: 
      newY--;
      break;
    case NORTH: 
      newZ--;
      break;
    case SOUTH: 
      newZ++;
      break;
    case EAST: 
      newX++;
      break;
    case WEST: 
      newX--;
    }
    return new Point3d(newX, newY, newZ);
  }
  
  public Point3d move(int dx, int dy, int dz)
  {
    int newX = this.x;int newY = this.y;int newZ = this.z;
    newX += dx;
    newY += dy;
    newZ += dz;
    
    return new Point3d(newX, newY, newZ);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Point3d that = (Point3d)o;
    return (this.x == that.x) && (this.y == that.y) && (this.z == that.z);
  }
  
  public int hashCode()
  {
    int hashX = this.x << 22;
    int hashY = this.y << 22 >> 10;
    int hashZ = this.z << 22 >> 20;
    return hashX + hashY + hashZ;
  }
  
  public BlockPos toBlockPos()
  {
    return new BlockPos(this.x, this.y, this.z);
  }
  
  public String toString()
  {
    return String.format("(%d,%d,%d)", new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z) });
  }
}
