package fr.mff.facmod.client.overlay;

import org.lwjgl.opengl.GL11;

import fr.mff.facmod.entity.CustomBossStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayGuardian {
	Minecraft mc = Minecraft.getMinecraft();
	private ResourceLocation boss = new ResourceLocation("faction:textures/gui/bossBar.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent.Pre event) {
		if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
			if (CustomBossStatus.bossName == null){
				return;
			}
			CustomBossStatus.update();
			event.setCanceled(true);
			FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
			ScaledResolution scaledresolution = new ScaledResolution(this.mc);
			int i = scaledresolution.getScaledWidth();
			short short1 = 182;
			int j = i / 2 - short1 / 2;
			int k = (int) (CustomBossStatus.healthScale * (short1 + 1));
			byte b0 = 12;

			String s = CustomBossStatus.bossName;
			fr.drawStringWithShadow(s, i / 2 - fr.getStringWidth(s) / 2, b0 + 7, 16777215);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(Gui.icons);

			this.mc.getTextureManager().bindTexture(this.boss);
			int size = (int) (181.0F * CustomBossStatus.healthScale);

		}
	}
}
