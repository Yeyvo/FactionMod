package fr.mff.facmod.items;

import fr.mff.facmod.wand.util.EnumFluidLock;
import fr.mff.facmod.wand.util.EnumLock;
import fr.mff.facmod.wand.util.RestrictedWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
public class ItemBuilderWand
  extends ItemBasicWand
{
  public ItemBuilderWand(RestrictedWand wand)
  {
    setMaxDamage(500);
    
    this.wand = wand;
  }
  
  public void nextMode(ItemStack itemStack, EntityPlayer player)
  {
    switch (getMode(itemStack))
    {
    case VERTICAL: 
      setMode(itemStack, EnumLock.HORIZONTAL);
      break;
    case HORIZONTAL: 
      setMode(itemStack, EnumLock.VERTICAL);
      break;
    default: 
      setMode(itemStack, EnumLock.HORIZONTAL);
    }
  }
  
  public void nextFluidMode(ItemStack itemStack, EntityPlayer player)
  {
    setFluidMode(itemStack, EnumFluidLock.STOPAT);
  }
  
  public EnumLock getFaceLock(ItemStack itemStack)
  {
    return EnumLock.NOLOCK;
  }
  
  public EnumLock getDefaultMode()
  {
    return EnumLock.HORIZONTAL;
  }
}
