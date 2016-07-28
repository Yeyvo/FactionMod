package fr.mff.facmod.client.overlay;

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

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class Overlays {

	public static final ResourceLocation BANNER = new ResourceLocation(FactionMod.MODID, "textures/overlay/banner.png");
	public static float alpha = 1.0f;

	public static void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		int i = 0;
		if(ConfigFaction.FACTION_OVERLAY) {
			String factionName = ((ClientProxy)FactionMod.proxy).factionName;
			if(!factionName.isEmpty()) {
				String str = EnumChatFormatting.UNDERLINE + I18n.format("overlay.faction", new Object[0]) + "§r : " + EnumChatFormatting.GOLD + factionName;
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10 + i * 15, 0xFFFFFF);
				i++;
			}
		}
		if(ConfigFaction.RANK_OVERLAY) {
			EnumRank rank = ((ClientProxy)FactionMod.proxy).rank;
			if(rank != EnumRank.WITHOUT_FACTION) {
				String str = EnumChatFormatting.UNDERLINE + I18n.format("overlay.rank", new Object[0]) + "§r : " + rank.getColor() + rank.getDisplay();
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10 + i * 15, 0xFFFFFF);
				i++;
			}
		}
		if(ConfigFaction.POWER_OVERLAY) {
			String powerLevel = ((ClientProxy)FactionMod.proxy).powerLevel;
			String str = EnumChatFormatting.UNDERLINE + I18n.format("overlay.power", new Object[0]) + "§r : " + powerLevel;
			Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10 + i * 15, 0xFFFFFF);
			i++;
		}
		if(ConfigFaction.LAND_OWNER_OVERLAY) {
			renderBanner();
		}
	}

	public static void renderBanner() {
		if(alpha > 0) {
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
			GL11.glColor4f(1.0f, 0.7f, 0.0f, alpha);
			return I18n.format("overlay.safeZone", new Object[0]);
		} else if(factionName.equalsIgnoreCase("warzone")) {
			GL11.glColor4f(1.0f, 0.0f, 0.0f, alpha);
			return I18n.format("overlay.warZone", new Object[0]);
		} else if(!factionName.isEmpty()) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
			return factionName;
		}
		GL11.glColor4f(1.0f, 0.0f, 0.6f, alpha);
		return I18n.format("overlay.freeLand", new Object[0]);
	}

}
