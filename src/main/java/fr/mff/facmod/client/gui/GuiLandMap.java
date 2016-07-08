package fr.mff.facmod.client.gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiLandMap extends GuiScreen {

	private String[] factionsNames;
	private int[] colors;
	
	private static final int[] usableColors = {0xE82626, 0x2AD930, 0x2A2AD9, 0x0008EF, 0xE2FA05};
											// dark-red  green     purple    blue      yellow

	public GuiLandMap(String[] factionsNames) {
		this.factionsNames = factionsNames;
		this.colors = new int[this.factionsNames.length];
		int colorIndex = 0;
		for(int i = 0; i < this.factionsNames.length; i++) {
			if(this.factionsNames[i].isEmpty()) {
				colors[i] = 0xFFFFFF;
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
					colors[i] = usableColors[colorIndex];
					colorIndex++;
				}
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawDefaultBackground();
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				
			}
		}
	}



}
