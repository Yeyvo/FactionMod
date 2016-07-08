package fr.mff.facmod.network;

import java.util.UUID;

import fr.mff.facmod.util.IWandItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWandActivate
  implements IMessage
{
  public boolean keyActive;
  public boolean keyFluidActive;
  
  public PacketWandActivate() {}
  
  public PacketWandActivate(boolean keyActive, boolean keyFluidActive)
  {
    this.keyActive = keyActive;
    this.keyFluidActive = keyFluidActive;
  }
  
  public void toBytes(ByteBuf buffer)
  {
    buffer.writeBoolean(this.keyActive);
    buffer.writeBoolean(this.keyFluidActive);
  }
  
  public void fromBytes(ByteBuf buffer)
  {
    this.keyActive = buffer.readBoolean();
    this.keyFluidActive = buffer.readBoolean();
  }
  
  public static class Handler
    extends GenericHandler<PacketWandActivate>
  {
    public void processMessage(PacketWandActivate packetWandActivate, MessageContext context)
    {
      EntityPlayerMP player = context.getServerHandler().playerEntity;
      UUID playerName = player.getUniqueID();
      if ((packetWandActivate.keyActive) && (player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() != null) && 
        ((player.getCurrentEquippedItem().getItem() instanceof IWandItem)))
      {
        ItemStack wandItemstack = player.getCurrentEquippedItem();
        IWandItem wandItem = (IWandItem)wandItemstack.getItem();
        wandItem.nextMode(wandItemstack, player);
        player.addChatMessage(new ChatComponentTranslation("bbw.chat.mode." + wandItem.getMode(wandItemstack).toString().toLowerCase(), new Object[0]));
      }
      if ((packetWandActivate.keyFluidActive) && (player.getCurrentEquippedItem() != null) && (player.getCurrentEquippedItem().getItem() != null) && 
        ((player.getCurrentEquippedItem().getItem() instanceof IWandItem)))
      {
        ItemStack wandItemstack = player.getCurrentEquippedItem();
        IWandItem wandItem = (IWandItem)wandItemstack.getItem();
        wandItem.nextFluidMode(wandItemstack, player);
        player.addChatMessage(new ChatComponentTranslation("bbw.chat.fluidmode." + wandItem.getFluidMode(wandItemstack).toString().toLowerCase(), new Object[0]));
      }
    }
  }
}
