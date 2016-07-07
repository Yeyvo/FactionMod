package fr.mff.facmod.core;

import fr.mff.facmod.blocks.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class FactionTabs extends CreativeTabs {

	public FactionTabs(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(BlockRegistry.FactionHome);
	}

}
