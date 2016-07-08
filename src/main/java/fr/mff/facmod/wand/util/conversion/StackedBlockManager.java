package fr.mff.facmod.wand.util.conversion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import fr.mff.facmod.wand.util.IWorldShim;
import fr.mff.facmod.wand.util.Point3d;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class StackedBlockManager
{
  private HashMap<String, ItemStack> cache;
  Method getStackedBlockMethod;
  String getStackedBlockMethodName;
  
  public StackedBlockManager()
  {
    this.cache = new HashMap();
    try
    {
      this.getStackedBlockMethodName = "func_180643_i";
      this.getStackedBlockMethod = Block.class.getDeclaredMethod(this.getStackedBlockMethodName, new Class[] { IBlockState.class });
      if (this.getStackedBlockMethod == null)
      {
        this.getStackedBlockMethodName = "createStackedBlock";
        this.getStackedBlockMethod = Block.class.getDeclaredMethod(this.getStackedBlockMethodName, new Class[] { IBlockState.class });
      }
      this.getStackedBlockMethod.setAccessible(true);
    }
    catch (NoSuchMethodException e)
    {
    }
  }
  
  public ItemStack getStackedBlock(IWorldShim world, Point3d blockPos)
  {
    Block block = world.getBlock(blockPos);
    IBlockState state = world.getWorld().getBlockState(blockPos.toBlockPos());
    String blockName = ((ResourceLocation)Block.blockRegistry.getNameForObject(block)).toString();
    String blockIdentifier = String.format("%s|%s", new Object[] { blockName, state.toString() });
    if (!this.cache.containsKey(blockIdentifier)) {
      this.cache.put(blockIdentifier, callGetStackedBlock(block, state));
    }
    return (ItemStack)this.cache.get(blockIdentifier);
  }
  
  private ItemStack callGetStackedBlock(Block block, IBlockState state)
  {
    try
    {
      Class<?> clazz = block.getClass();
      while (clazz != null)
      {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
          if ((method.getName().equals(this.getStackedBlockMethodName)) || (method.getName().equals("createStackedBlock")))
          {
            this.getStackedBlockMethod = method;
            break;
          }
        }
        clazz = clazz.getSuperclass();
      }
      if (this.getStackedBlockMethod != null)
      {
        this.getStackedBlockMethod.setAccessible(true);
        return (ItemStack)this.getStackedBlockMethod.invoke(block, new Object[] { state });
      }
    }
    catch (IllegalAccessException e)
    {
    }
    catch (InvocationTargetException e)
    {
    }
    catch (NoClassDefFoundError e)
    {
    }
    return null;
  }
}
