package fr.mff.facmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mff.facmod.client.gui.container.ContainerFactionGuardian;
import fr.mff.facmod.client.overlay.DisplayHelper;
import fr.mff.facmod.entity.EntityFactionGuardian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFactionGuardian extends GuiContainer {
	private EntityFactionGuardian golem;
	private final ResourceLocation background = new ResourceLocation("faction:textures/gui/FactionGuardian.png");
	private FontRenderer fr;
	public float xSizeFloat;
	public float ySizeFloat;

	public GuiFactionGuardian(EntityFactionGuardian entity, InventoryPlayer inventory) {
		super(new ContainerFactionGuardian(entity, inventory));
		this.fr = Minecraft.getMinecraft().fontRendererObj;
		this.golem = entity;
		this.xSize = 238;
		this.ySize = 227;
	}

	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		this.xSizeFloat = p_73863_1_;
		this.ySizeFloat = p_73863_2_;
	}

	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		this.mc.renderEngine.bindTexture(this.background);
		drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		float scaledXp = 240.0F * (this.golem.getSubLevel() / this.golem.getRequiredXP());
		drawTexturedModalRect(x + 9, y + 20, 0, 227, (int) scaledXp, 11);
		DisplayHelper.renderEntity(x + 44, y + 110, 30, x + 51 - this.xSizeFloat, y + 75 - 50 - this.ySizeFloat, this.golem);
		this.fr.drawString("Niveau " + this.golem.getLevel(),
				x + (this.xSize / 2 - this.fr/* 60 */ .getStringWidth("Niveau " + this.golem.getLevel()) / 2), y + 21,
				16777215, true);
		this.fr.drawString(this.golem.getSubLevel() + "/" + (int) this.golem.getRequiredXP(), x + 11, y + 38, 16777215,
				true);
		this.fr.drawString("" + this.golem.getSpeed(), x + 103, y + 120, 16777215, true);

		this.fr.drawString("" + this.golem.getDamages(), x + 151, y + 120, 16777215, true);

		this.fr.drawString("" + this.golem.getLife(), x + 195, y + 120, 16777215, true);
	}
}
