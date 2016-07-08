package fr.mff.facmod.items;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.entity.EntityDynamite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDynamite
  extends Item
{
  
  public ItemDynamite()
  {
    this.maxStackSize = 64;
    setCreativeTab(FactionMod.factionTabs);
  }
  
  public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
  {
    if (!par3EntityPlayer.capabilities.isCreativeMode) {
      par1ItemStack.stackSize -= 1;
    }
    par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    if (!par2World.isRemote) {
    	par2World.spawnEntityInWorld(new EntityDynamite(par2World, par3EntityPlayer));    }
    return par1ItemStack;
  }
}
