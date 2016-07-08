package fr.mff.facmod.client.gui.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotChestWatcher extends Slot {

	public SlotChestWatcher(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	public boolean isItemValid(ItemStack item) {
		return true;
	}

	public ItemStack decrStackSize(int i) {
		return null;
	}
}
