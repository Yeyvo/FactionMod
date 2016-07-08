package fr.mff.facmod.wand.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class RestrictedWand
  implements IWand
{
  protected int blocklimit = 0;
  
  public RestrictedWand(int limit)
  {
    this.blocklimit = limit;
  }
  
  public int getMaxBlocks(ItemStack itemStack)
  {
    return Math.min(itemStack.getMaxDamage() - itemStack.getItemDamage(), this.blocklimit);
  }
  
  public boolean placeBlock(ItemStack itemStack, EntityLivingBase entityLivingBase)
  {
    itemStack.damageItem(1, entityLivingBase);
    if ((itemStack.stackSize > 0) && (itemStack.getItemDamage() == itemStack.getMaxDamage())) {
      itemStack.stackSize = 0;
    }
    return true;
  }
}
