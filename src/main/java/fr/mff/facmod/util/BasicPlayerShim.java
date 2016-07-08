package fr.mff.facmod.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BasicPlayerShim
  implements IPlayerShim
{
  private EntityPlayer player;
  
  public BasicPlayerShim(EntityPlayer player)
  {
    this.player = player;
  }
  
  public int countItems(ItemStack itemStack)
  {
    int total = 0;
    if ((itemStack == null) || (this.player.inventory == null) || (this.player.inventory.mainInventory == null)) {
      return 0;
    }
    for (ItemStack inventoryStack : this.player.inventory.mainInventory) {
      if ((inventoryStack != null) && (itemStack.isItemEqual(inventoryStack))) {
        total += Math.max(0, inventoryStack.stackSize);
      }
    }
    return itemStack.stackSize > 0 ? total / itemStack.stackSize : 0;
  }
  
  public boolean useItem(ItemStack itemStack)
  {
    if ((itemStack == null) || (this.player.inventory == null) || (this.player.inventory.mainInventory == null)) {
      return false;
    }
    int toUse = itemStack.stackSize;
    for (int i = this.player.inventory.mainInventory.length - 1; i >= 0; i--)
    {
      ItemStack inventoryStack = this.player.inventory.mainInventory[i];
      if ((inventoryStack != null) && (itemStack.isItemEqual(inventoryStack)))
      {
        if (inventoryStack.stackSize < toUse)
        {
          inventoryStack.stackSize = 0;
          toUse -= inventoryStack.stackSize;
        }
        else
        {
          inventoryStack.stackSize -= toUse;
          toUse = 0;
        }
        if (inventoryStack.stackSize == 0) {
          this.player.inventory.setInventorySlotContents(i, null);
        }
        this.player.inventoryContainer.detectAndSendChanges();
        if (toUse <= 0) {
          return true;
        }
      }
    }
    return false;
  }
  
  public ItemStack getNextItem(Block block, int meta)
  {
    for (int i = this.player.inventory.mainInventory.length - 1; i >= 0; i--) {
      ItemStack localItemStack = this.player.inventory.mainInventory[i];
    }
    return null;
  }
  
  public Point3d getPlayerPosition()
  {
    return new Point3d((int)this.player.posX, (int)this.player.posY, (int)this.player.posZ);
  }
  
  public EntityPlayer getPlayer()
  {
    return this.player;
  }
  
  public boolean isCreative()
  {
    return this.player.capabilities.isCreativeMode;
  }
}
