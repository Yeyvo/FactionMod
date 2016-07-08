package fr.mff.facmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mff.facmod.client.gui.container.ContainerChestWatcher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiChestWatcher extends GuiContainer {

	static final ResourceLocation resource = new ResourceLocation("faction:textures/gui/chestwatcher.png");

	public GuiChestWatcher(TileEntity tileentity) {
		super(new ContainerChestWatcher(tileentity));
		this.xSize = 256;
		this.ySize = 177;

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.mc.renderEngine.bindTexture(resource);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, 256, 256);
		//drawTexturedModalRect(82, 35, 0, 0, 256, 256);

	}

}
