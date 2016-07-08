package fr.mff.facmod.blocks;

import java.util.Set;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.tileentities.TileEntityBlockXray;
import fr.mff.facmod.tileentities.TileEntityHomeBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockXray extends Block {

	public BlockXray(Material material) {
		super(material);
		setHardness(4.0F);
		setResistance(4.0F);
	    setHarvestLevel("pickaxe", 1);
		setCreativeTab(FactionMod.factionTabs);
	}
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

	public boolean hasTileEntity() {
		return true;
	}

	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBlockXray();
	}
}
