package fr.mff.facmod.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import fr.mff.facmod.FactionMod;

public class BlockHomeBase extends Block {

	public BlockHomeBase(Material material) {
		super(material);
		this.setHardness(100.F);
		this.setResistance(1000.F);
		this.setLightLevel(1.0F);
		this.setCreativeTab(FactionMod.factionTabs);
	}

}
