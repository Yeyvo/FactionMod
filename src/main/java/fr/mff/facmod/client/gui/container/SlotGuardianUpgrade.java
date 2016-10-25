package fr.mff.facmod.client.gui.container;

import fr.mff.facmod.entity.EntityFactionGuardian;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGuardianUpgrade extends Slot {
	EntityFactionGuardian golem;

	public SlotGuardianUpgrade(IInventory inventory, int id, int x, int y, EntityFactionGuardian golem) {
		super(inventory, id, x, y);

		this.golem = golem;
	}

	public boolean isItemValid(ItemStack stack)
  {
	return false;
    /*if ((stack.getItem() instanceof ItemGuardianUpgrade))
    {
     if (this.golem.getLevel() >= ((ItemGuardianUpgrade)stack.getItem()).getRequiredLevel())
         return true; }
   return false;
 	 }*/
 }
}
