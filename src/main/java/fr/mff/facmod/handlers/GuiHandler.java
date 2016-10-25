package fr.mff.facmod.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import fr.mff.facmod.client.gui.GuiChestWatcher;
import fr.mff.facmod.client.gui.GuiFactionGuardian;
import fr.mff.facmod.client.gui.GuiHomeBase;
import fr.mff.facmod.client.gui.container.ContainerChestWatcher;
import fr.mff.facmod.client.gui.container.ContainerFactionGuardian;
import fr.mff.facmod.entity.EntityFactionGuardian;
import fr.mff.facmod.inventory.ContainerHomeBase;
import fr.mff.facmod.tileentities.TileEntityHomeBase;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID)
		{
		case 0:
			return new ContainerHomeBase((TileEntityHomeBase)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		case 1:
			return new ContainerChestWatcher(world.getTileEntity(new BlockPos(x, y, z)));
		case 7:
			return new ContainerFactionGuardian((EntityFactionGuardian)world.getEntityByID(x), player.inventory);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID)
		{
		case 0:
			return new GuiHomeBase((TileEntityHomeBase)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		case 1:
			return new GuiChestWatcher(world.getTileEntity(new BlockPos(x, y, z)));
		case 7: 
			return new GuiFactionGuardian((EntityFactionGuardian)world.getEntityByID(x), player.inventory);
		}
		return null;
	}

}
