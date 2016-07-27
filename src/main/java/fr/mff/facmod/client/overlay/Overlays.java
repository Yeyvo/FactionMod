package fr.mff.facmod.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class Overlays {

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
			String factionName = ((ClientProxy)FactionMod.proxy).landOwner;
			if(factionName.equalsIgnoreCase("safezone")) {
				String str = EnumChatFormatting.YELLOW + I18n.format("overlay.safeZone", new Object[0]);
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10 + i * 15, 0xFFFFFF);
				i++;
			} else if(factionName.equalsIgnoreCase("warzone")) {
				String str = EnumChatFormatting.DARK_RED + I18n.format("overlay.warZone", new Object[0]);
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10 + i * 15, 0xFFFFFF);
				i++;
			} else if(!factionName.isEmpty()) {
				String str = EnumChatFormatting.UNDERLINE + I18n.format("overlay.landOwner", new Object[0]) + "§r : " + EnumChatFormatting.GOLD + factionName;
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10 + i * 15, 0xFFFFFF);
				i++;
			}
		}
	}

}
