package fr.mff.facmod.handlers;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import fr.mff.facmod.wand.util.BasicPlayerShim;
import fr.mff.facmod.wand.util.BasicWorldShim;
import fr.mff.facmod.wand.util.CreativePlayerShim;
import fr.mff.facmod.wand.util.IPlayerShim;
import fr.mff.facmod.wand.util.IWand;
import fr.mff.facmod.wand.util.IWandItem;
import fr.mff.facmod.wand.util.IWorldShim;
import fr.mff.facmod.wand.util.Point3d;
import fr.mff.facmod.wand.util.WandWorker;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockEvents
{
  @SubscribeEvent
  public void blockHighlightEvent(DrawBlockHighlightEvent event)
  {
    if ((event.currentItem != null) && ((event.currentItem.getItem() instanceof IWandItem)) && (event.target != null) && (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK))
    {
      IPlayerShim playerShim = new BasicPlayerShim(event.player);
      if (event.player.capabilities.isCreativeMode) {
        playerShim = new CreativePlayerShim(event.player);
      }
      IWorldShim worldShim = new BasicWorldShim(event.player.getEntityWorld());
      if ((event.currentItem.getItem() instanceof IWandItem))
      {
        IWandItem wandItem = (IWandItem)event.currentItem.getItem();
        IWand wand = wandItem.getWand();
        
        WandWorker worker = new WandWorker(wand, playerShim, worldShim);
        
        Point3d clickedPos = new Point3d(event.target.getBlockPos().getX(), event.target.getBlockPos().getY(), event.target.getBlockPos().getZ());
        ItemStack sourceItems = worker.getProperItemStack(worldShim, playerShim, clickedPos);
        if ((sourceItems != null) && ((sourceItems.getItem() instanceof ItemBlock)))
        {
          int numBlocks = Math.min(wand.getMaxBlocks(event.currentItem), playerShim.countItems(sourceItems));
          
          LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, event.target.sideHit, numBlocks, wandItem.getMode(event.currentItem), wandItem.getFaceLock(event.currentItem), wandItem.getFluidMode(event.currentItem));
          if (blocks.size() > 0)
          {
            GlStateManager.disableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GL11.glLineWidth(2.5F);
            for (Point3d block : blocks)
            {
              Block blockb = Blocks.bedrock;
              EntityPlayer player = event.player;
              double partialTicks = event.partialTicks;
              double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
              double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
              double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
              RenderGlobal.drawSelectionBoundingBox(blockb.getSelectedBoundingBox(worldShim.getWorld(), new BlockPos(block.x, block.y, block.z)).contract(0.005D, 0.005D, 0.005D).offset(-d0, -d1, -d2));
            }
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.depthMask(false);
          }
        }
      }
    }
  }
}
