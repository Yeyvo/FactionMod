package fr.mff.facmod.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class FactionOverlay {
	
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Text e) {
		String factionName = ((ClientProxy)FactionMod.proxy).factionName;
		String str = EnumChatFormatting.UNDERLINE + "Faction§r : " + (factionName.equals("") ? "aucune" : EnumChatFormatting.GOLD + factionName);
		Minecraft.getMinecraft().fontRendererObj.drawString(str, 10, 10, 0xFFFFFF);
	}

}
