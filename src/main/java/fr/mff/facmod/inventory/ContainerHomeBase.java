package fr.mff.facmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fr.mff.facmod.tileentities.TileEntityHomeBase;

public class ContainerHomeBase extends Container {

	private TileEntityHomeBase tile;

	public ContainerHomeBase(TileEntityHomeBase tileEntity, InventoryPlayer playerInventory) {
		this.tile = tileEntity;
		if(!tile.getWorld().isRemote) {
			tile.openInventory(playerInventory.player);
		}

		for (int j = 0; j < 3; ++j)
		{
			for (int k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new SlotOutput(tile, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		for (int l = 0; l < 3; ++l)
		{
			for (int j1 = 0; j1 < 9; ++j1)
			{
				this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 - 18));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1)
		{
			this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 - 18));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tile.isUseableByPlayer(playerIn);
	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 27)
			{
				if (!this.mergeItemStack(itemstack1, 3 * 9, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if(index < 54) {
				if(!this.mergeItemStack(itemstack1, 54, this.inventorySlots.size(), false)) {
					return null;
				}
			} else if(!this.mergeItemStack(itemstack1, 27, 54, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

}
