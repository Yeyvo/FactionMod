package fr.mff.facmod.blocks;

import fr.mff.facmod.FactionMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class FactionHome extends Block {

	public FactionHome(Material material) {
		super(material);
		this.setHardness(100.F);
		this.setResistance(1000.F);
		this.setLightLevel(1.0F);
		this.setUnlocalizedName("FactionHome");
		this.setCreativeTab(FactionMod.FactionTabs);
	}

}
