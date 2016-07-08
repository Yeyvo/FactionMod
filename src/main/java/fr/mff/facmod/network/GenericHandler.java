package fr.mff.facmod.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class GenericHandler<REQ extends IMessage>
  implements IMessageHandler<REQ, IMessage>
{
  public IMessage onMessage(REQ message, MessageContext ctx)
  {
    processMessage(message, ctx);
    return null;
  }
  
  public abstract void processMessage(REQ paramREQ, MessageContext paramMessageContext);
}
