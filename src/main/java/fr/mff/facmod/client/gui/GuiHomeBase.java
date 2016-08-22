package fr.mff.facmod.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import fr.mff.facmod.inventory.ContainerHomeBase;
import fr.mff.facmod.tileentities.TileEntityHomeBase;

public class GuiHomeBase extends GuiContainer {
	
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	
	private TileEntityHomeBase tile;

	public GuiHomeBase(TileEntityHomeBase tile, InventoryPlayer playerInventory) {
		super(new ContainerHomeBase(tile, playerInventory));
		this.tile = tile;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(this.tile.getDisplayName().getUnformattedText(), 8, 6, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, 3 * 18 + 17);
        this.drawTexturedModalRect(i, j + 3 * 18 + 17, 0, 126, this.xSize, 96);
    }

}
