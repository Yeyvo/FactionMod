package fr.mff.facmod.renderer;

import fr.mff.facmod.items.ItemRegistry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDynamite
  extends Render
{
  private final RenderItem field_177083_e;
  
  public RenderDynamite(RenderManager p_i46137_1_, RenderItem p_i46137_3_)
  {
    super(p_i46137_1_);
    this.field_177083_e = p_i46137_3_;
  }
  
  public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
  {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x, (float)y, (float)z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(0.5F, 0.5F, 0.5F);
    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    bindTexture(TextureMap.locationBlocksTexture);
    this.field_177083_e.renderItem(func_177082_d(entity), ItemCameraTransforms.TransformType.GROUND);
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
  }
  
  public ItemStack func_177082_d(Entity p_177082_1_)
  {
    return new ItemStack(ItemRegistry.dynamite, 1, 0);
  }
  
  protected ResourceLocation getEntityTexture(Entity entity)
  {
    return TextureMap.locationBlocksTexture;
  }
}
