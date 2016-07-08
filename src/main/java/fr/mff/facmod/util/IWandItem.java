package fr.mff.facmod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;


public abstract interface IWandItem
{
  public abstract EnumLock getMode(ItemStack paramItemStack);
  
  public abstract void nextMode(ItemStack paramItemStack, EntityPlayer paramEntityPlayer);
  
  public abstract EnumFluidLock getFluidMode(ItemStack paramItemStack);
  
  public abstract void nextFluidMode(ItemStack paramItemStack, EntityPlayer paramEntityPlayer);
  
  public abstract IWand getWand();
  
  public abstract EnumLock getFaceLock(ItemStack paramItemStack);
}
