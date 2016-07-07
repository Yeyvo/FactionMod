package fr.mff.facmod.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.Random;

import fr.mff.facmod.FactionMod;

public class BlockHomeBase extends Block {

	public BlockHomeBase(Material material) {
		super(material);
		this.setHardness(60.F);
		this.setResistance(1000.F);
		this.setLightLevel(1.0F);
		this.setCreativeTab(FactionMod.factionTabs);
	}

	
    public int quantityDropped(Random random)
    {
        return 0;
    }
    protected boolean canSilkHarvest()
    {
        return false;
    }
}
