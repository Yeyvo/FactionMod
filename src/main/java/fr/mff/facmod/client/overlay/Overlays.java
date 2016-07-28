package fr.mff.facmod.client.overlay;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class Overlays {

	public static final ResourceLocation BANNER = new ResourceLocation(FactionMod.MODID, "textures/overlay/banner.png");
	public static final ResourceLocation PANEL = new ResourceLocation(FactionMod.MODID, "textures/overlay/panel.png");
	public static float alpha = 1.0f;

	public static void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		renderFactionPanel();
		renderBanner();
	}

	public static void renderFactionPanel() {
		String factionName = ((ClientProxy)FactionMod.proxy).factionName;
		if(!factionName.isEmpty() || ConfigFaction.POWER_OVERLAY) {
			
			List<String> toPrint = Lists.newArrayList();
			if(ConfigFaction.FACTION_OVERLAY) {
				toPrint.add(EnumChatFormatting.GOLD + factionName);
			}
			if(ConfigFaction.RANK_OVERLAY) {
				EnumRank rank = ((ClientProxy)FactionMod.proxy).rank;
				toPrint.add(rank.getColor() + rank.getDisplay());
			}
			if(ConfigFaction.POWER_OVERLAY) {
				toPrint.add(I18n.format("overlay.power", new Object[0]) + " : " + ((ClientProxy)FactionMod.proxy).powerLevel);
			}
			int maxlength = 0;
			for(String s : toPrint) {
				int length = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s);
				if(length > maxlength) {
					maxlength = length;
				}
			}
			float xScale = maxlength / 89.0F;
			Minecraft.getMinecraft().getTextureManager().bindTexture(PANEL);
			GL11.glPushMatrix();
			GL11.glScalef(xScale, 1.0f, 1.0f);
			
			int infoCount = 0;
			Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 100, 7, 110, 8, 100, 40);
			for(int i = 0; i < toPrint.size(); i++) {
				Gui.drawScaledCustomSizeModalRect(0, 29 * infoCount + 7, 0, 7, 100, 26, 110, 29, 100, 40);
				infoCount++;
			}
			Gui.drawScaledCustomSizeModalRect(0, 29 * infoCount + 7, 0, 33, 100, 7, 110, 8, 100, 40);
			GL11.glPopMatrix();
			
			infoCount = 0;
			for(String s : toPrint) {
				Minecraft.getMinecraft().fontRendererObj.drawString(s, (int)(55 * xScale) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) / 2), 18 + infoCount * 29, 0xFFFFFF);
				infoCount++;
			}
		}
	}

	public static void renderBanner() {
		if(ConfigFaction.LAND_OWNER_OVERLAY && alpha > 0) {
			GL11.glPushMatrix();
			String factionName = getLandOwnerAndSetColor();
			int x = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2;
			Minecraft.getMinecraft().getTextureManager().bindTexture(BANNER);
			Gui.drawScaledCustomSizeModalRect(x - 125, 0, 0, 0, 100, 30, 250, 75, 100, 30);
			GL11.glScalef(1.5f, 1.5f, 1.0f);
			int color = 0xFFFFFF | ((int)(alpha * 255) << 24);
			Minecraft.getMinecraft().fontRendererObj.drawString(factionName, (int)(x / 1.5 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(factionName) / 2), (int)(34 / 1.5), color);

			GL11.glPopMatrix();

			if(alpha - 0.1f < 0.0f) {
				alpha = 0.0f;
			} else {
				alpha -= 0.01f;
			}

		}
	}

	private static String getLandOwnerAndSetColor() {
		String factionName = ((ClientProxy)FactionMod.proxy).landOwner;
		if(factionName.equalsIgnoreCase("safezone")) {
			GL11.glColor4f(1.0f, 1.0f, 0.0f, alpha);
			return I18n.format("overlay.safeZone", new Object[0]);
		} else if(factionName.equalsIgnoreCase("warzone")) {
			GL11.glColor4f(1.0f, 0.0f, 0.0f, alpha);
			return I18n.format("overlay.warZone", new Object[0]);
		} else if(!factionName.isEmpty()) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
			return factionName;
		}
		GL11.glColor4f(1.0f, 0.0f, 1.0f, alpha);
		return I18n.format("overlay.freeLand", new Object[0]);
	}

}
