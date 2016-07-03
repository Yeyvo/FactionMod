package fr.mff.facmod.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.proxy.ClientProxy;

public class ClientEventHandler {
	
	/**
	 * Used for debugging
	 * @param e
	 */
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		String factionName = ((ClientProxy)FactionMod.proxy).factionName;
		String str = EnumChatFormatting.UNDERLINE + "Faction§r : " + (factionName.equals("") ? "aucune" : EnumChatFormatting.GOLD + factionName);
		Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10, 0xFFFFFF);
	}

}
