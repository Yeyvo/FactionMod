package fr.mff.facmod.handlers;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.mff.facmod.client.overlay.Overlays;
import fr.mff.facmod.config.ConfigFaction;

public class ClientEventHandler {

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		Overlays.onRenderGameOverlay(e);
	}


	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		ConfigFaction.onConfigChanged(event);
	}



}
