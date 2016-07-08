package fr.mff.facmod.handlers;

import fr.mff.facmod.client.gui.GuiChestWatcher;
import fr.mff.facmod.client.gui.container.ContainerChestWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID)
		{
		case 1:
			return new ContainerChestWatcher(world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID)
		{
		case 1:
			return new GuiChestWatcher(world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

}
