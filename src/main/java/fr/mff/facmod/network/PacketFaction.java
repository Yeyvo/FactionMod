package fr.mff.facmod.network;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFaction implements IMessage {
	
	public String facName;
	
	public PacketFaction() { }
	
	public PacketFaction(String facName) {
		this.facName = facName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.facName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, facName);
	}
	
	public static class Handler implements IMessageHandler<PacketFaction, IMessage> {
		
		@Override
		public IMessage onMessage(PacketFaction message, MessageContext ctx) {
			if(FactionMod.proxy instanceof ClientProxy) {
				((ClientProxy)FactionMod.proxy).factionName = message.facName;
			}
			return null;
		}
		
	}

}