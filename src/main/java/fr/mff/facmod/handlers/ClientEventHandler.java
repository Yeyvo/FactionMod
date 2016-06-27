package fr.mff.facmod.handlers;

import fr.mff.facmod.core.extendedProperties.ExtendedPropertieFaction;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {
	
	/**
	 * Used for debugging
	 * @param e
	 */
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		ExtendedPropertieFaction prop = ExtendedPropertieFaction.get(Minecraft.getMinecraft().thePlayer);
		String str = "Faction : " + (prop.factionName.equals("") ? "aucune" : prop.factionName);
		Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10, 0xFFFFFF);
	}

}
