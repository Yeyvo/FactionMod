package fr.mff.facmod.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CreativePlayerShim
  extends BasicPlayerShim
  implements IPlayerShim
{
  public CreativePlayerShim(EntityPlayer player)
  {
    super(player);
  }
  
  public int countItems(ItemStack itemStack)
  {
    return Integer.MAX_VALUE;
  }
  
  public boolean useItem(ItemStack itemStack)
  {
    return true;
  }
  
  public ItemStack getNextItem(Block block, int meta)
  {
    return new ItemStack(block, 1, meta);
  }
}
