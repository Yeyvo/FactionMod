package fr.mff.facmod.client.gui;

import java.util.Set;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Sets;

import fr.mff.facmod.FactionMod;

public class GuiLandMap extends GuiScreen {

	public static final ResourceLocation TEXTURE = new ResourceLocation(FactionMod.MODID, "textures/gui/landMapGUI.png");

	private static final int colorsX = 206;

	private String[] factionsNames;
	private short[] colors;

	public GuiLandMap(String[] factionsNames) {
		this.factionsNames = factionsNames;
		this.colors = new short[this.factionsNames.length];
		short colorIndex = 2;
		for(int i = 0; i < this.factionsNames.length; i++) {
			if(this.factionsNames[i].isEmpty()) {
				colors[i] = -1;
			} else if(this.factionsNames[i].equalsIgnoreCase("warzone")) {
				this.factionsNames[i] = "War zone";
				colors[i] = 0;
			} else if(this.factionsNames[i].equalsIgnoreCase("safezone")) {
				this.factionsNames[i] = "Safe zone";
				colors[i] = 1;
			} else {
				boolean flag = true;
				for(int l = 0; l < i; l++) {
					if(this.factionsNames[l].equalsIgnoreCase(this.factionsNames[i])) {
						colors[i] = colors[l];
						flag = false;
						break;
					}
				}
				if(flag) {
					colors[i] = colorIndex;
					colorIndex++;
				}
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawDefaultBackground();
		int centerX = (this.width - 205) / 2;
		int centerY = (this.height - 205) / 2;
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(centerX, centerY, 0, 0, 205, 205);
		if(this.colors.length >= 25) {
			for(int i = 0; i < 5; i++) {
				for(int j = 0; j < 5; j++) {
					if(colors[i * 5 + j] >= 0) {
						Gui.drawScaledCustomSizeModalRect(centerX + 1 + i * 41, centerY + 1 + j * 41, colorsX + this.colors[i * 5 + j], 0, 1, 1, 40, 40, 256, 256);
					}
				}
			}
		}
		Set<Short> did = Sets.newHashSet();
		int y = 0;
		int x = 0;
		for(int i = 0; i < colors.length; i++) {
			if(colors[i] > -1 && !did.contains(colors[i])) {
				Gui.drawScaledCustomSizeModalRect(centerX + 215 + x * 120, centerY + 20 + 20 * y, colorsX + this.colors[i], 0, 1, 1, 10, 10, 256, 256);
				did.add(colors[i]);
				y++;
			}
			if(y > 9) {
				x++;
				y = 0;
			}
		}
		y = 0;
		x = 0;
		did.clear();
		for(int i = 0; i < colors.length; i++) {
			if(colors[i] > -1 && !did.contains(colors[i])) {
				this.drawString(fontRendererObj, factionsNames[i], centerX + 230 + x * 120, centerY + 20 + 20 * y, 0xFFFFFF);
				did.add(colors[i]);
				y++;
			}
			if(y > 9) {
				x++;
				y = 0;
			}
		}
		this.drawCenteredString(fontRendererObj, "North", centerX + 102, centerY + 10, 0xFFFFFF);
		this.drawCenteredString(fontRendererObj, "South", centerX + 102, centerY + 185, 0xFFFFFF);
		this.drawCenteredString(fontRendererObj, "West", centerX + 20, centerY + 100, 0xFFFFFF);
		this.drawCenteredString(fontRendererObj, "East", centerX + 185, centerY + 100, 0xFFFFFF);
	}
}
