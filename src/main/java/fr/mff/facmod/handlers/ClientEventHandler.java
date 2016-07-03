package fr.mff.facmod.handlers;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.mff.facmod.client.overlay.FactionOverlay;

public class ClientEventHandler {
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		FactionOverlay.onRenderGameOverlay(e);
	}

}
