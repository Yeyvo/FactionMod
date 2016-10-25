package fr.mff.facmod.renderer;

import org.lwjgl.opengl.GL11;

import fr.mff.facmod.entity.EntityFactionGuardian;
import fr.mff.facmod.model.ModelZygarde;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderZygarde extends RenderLiving {

	public static final ResourceLocation Zygarde_texture = new ResourceLocation("faction", "textures/models/Zygarde.png");
	public static ModelZygarde modelZygarde = new ModelZygarde();	
	public static float modelHeight = 2F;
	
	public RenderZygarde(RenderManager renderManager)
    {
        super(renderManager, modelZygarde, 1F);
    }
	
	@Override
	public void doRender(EntityLiving _entity, double posX, double posY, double posZ, float var8, float var9) {
		EntityFactionGuardian entity = (EntityFactionGuardian) _entity;
				
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.doRender(_entity, posX, posY, posZ, var8, var9);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entityliving, float f)
	{
		GL11.glRotatef(180F, 0, 1F, 0F);
		GL11.glRotatef(180F, 0, 0, 1F);
		GL11.glTranslatef(0, modelHeight, 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return Zygarde_texture;
	}
}