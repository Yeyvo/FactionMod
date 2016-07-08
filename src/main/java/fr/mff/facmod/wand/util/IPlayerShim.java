package fr.mff.facmod.wand.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract interface IPlayerShim
{
  public abstract int countItems(ItemStack paramItemStack);
  
  public abstract boolean useItem(ItemStack paramItemStack);
  
  public abstract ItemStack getNextItem(Block paramBlock, int paramInt);
  
  public abstract Point3d getPlayerPosition();
  
  public abstract EntityPlayer getPlayer();
  
  public abstract boolean isCreative();
}
