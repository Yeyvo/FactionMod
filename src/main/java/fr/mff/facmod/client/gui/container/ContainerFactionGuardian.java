/*    */ package fr.mff.facmod.client.gui.container;
/*    */

import fr.mff.facmod.entity.EntityFactionGuardian;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.ItemStack;

/*    */
/*    */ public class ContainerFactionGuardian/*    */ extends Container
/*    */ {
	/*    */ private EntityFactionGuardian golem;

	/*    */
	/*    */ public ContainerFactionGuardian(EntityFactionGuardian entity, InventoryPlayer inventory)
	/*    */ {
		/* 21 */ this.golem = entity;
		/* 22 */ addSlotToContainer(new SlotGuardianUpgrade(entity, 0, 84, 53, this.golem));
		/* 23 */ addSlotToContainer(new SlotGuardianUpgrade(entity, 1, 102, 53, this.golem));
		/* 24 */ addSlotToContainer(new SlotGuardianUpgrade(entity, 2, 120, 53, this.golem));
		/*    */
		/*    */
		/* 31 */ bindPlayerInventory(inventory);
		/*    */ }

	/*    */
	/*    */ private void bindPlayerInventory(InventoryPlayer inventory)
	/*    */ {
		/* 36 */ for (int i = 0; i < 3; i++) {
			/* 37 */ for (int j = 0; j < 9; j++) {
				/* 38 */ addSlotToContainer(new Slot(inventory, j + i * 9 + 9, j * 18 + 39, 145 + i * 18));
				/*    */ }
			/*    */ }
		/*    */
		/*    */
		/* 43 */ for (int i = 0; i < 9; i++) {
			/* 44 */ addSlotToContainer(new Slot(inventory, i, i * 18 + 39, 203));
			/*    */ }
		/*    */ }

	/*    */
	/*    */ public boolean canInteractWith(EntityPlayer player)
	/*    */ {
		/* 50 */ return this.golem.isUseableByPlayer(player);
		/*    */ }

	/*    */
	/*    */ public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		/* 54 */ return null;
		/*    */ }
	/*    */ }

/*
 * Location: C:\Users\Mosca421\Desktop\Mods\Logiciel
 * Utiles\PalaClient-deggobf.jar!\fr\paladium\palamod\common\gui\
 * ContainerGuardianGolem.class Java compiler version: 6 (50.0) JD-Core Version:
 * 0.7.1
 */