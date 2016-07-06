package fr.mff.facmod.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class Overlays {

	public static void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		if(ConfigFaction.FACTION_OVERLAY) {
			String factionName = ((ClientProxy)FactionMod.proxy).factionName;
			if(!factionName.equals("")) {
				String str = EnumChatFormatting.UNDERLINE + "Faction§r : " + EnumChatFormatting.GOLD + factionName;
				Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10, 0xFFFFFF);
			}
		}
	}

}
