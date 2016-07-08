package fr.mff.facmod.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerChestWatcher extends Container {

	IInventory inventory;

	public ContainerChestWatcher(TileEntity tileentity) {

		this.inventory = (IInventory) tileentity;
		int a = 0;
		int b = 0;
		for (a = 0; a < 108; a++) {
			if (a % 12 == 0) {
				b++;
			}
			int e = a % 12 + 1;
			if (a < this.inventory.getSizeInventory()) {
				this.addSlotToContainer(new SlotChestWatcher(this.inventory, a, e * 18 - 6, b * 18 - 10));
			} else {
				this.addSlotToContainer(new SlotChestWatcher(new ChestWatcherInventory(), 0, e * 18 - 6, b * 18 - 10));

			}
		}
	}

	public boolean canInteractWith(EntityPlayer player) {
		return this.inventory.isUseableByPlayer(player);
	}

	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		return null;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return null;
	}

}
