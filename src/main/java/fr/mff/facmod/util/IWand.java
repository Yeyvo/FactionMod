package fr.mff.facmod.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract interface IWand
{
  public abstract int getMaxBlocks(ItemStack paramItemStack);
  
  public abstract boolean placeBlock(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
}
