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
		if(ConfigFaction.FACTION_OVERLAY) {
			String factionName = ((ClientProxy)FactionMod.proxy).factionName;
			if(!factionName.equals("")) {
				String str = EnumChatFormatting.UNDERLINE + I18n.format("overlay.faction", new Object[0]) + "§r : " + EnumChatFormatting.GOLD + factionName;
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10, 0xFFFFFF);
			}
		}
		if(ConfigFaction.RANK_OVERLAY) {
			EnumRank rank = ((ClientProxy)FactionMod.proxy).rank;
			if(rank != EnumRank.WITHOUT_FACTION) {
				String str = EnumChatFormatting.UNDERLINE + I18n.format("overlay.rank", new Object[0]) + "§r : " + rank.getColor() + rank.getDisplay();
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 25, 0xFFFFFF);
			}
		}
	}

}
