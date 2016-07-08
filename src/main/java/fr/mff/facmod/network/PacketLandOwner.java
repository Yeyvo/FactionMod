package fr.mff.facmod.network;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLandOwner implements IMessage {
	
	private String name;
	
	public PacketLandOwner() {}
	
	public PacketLandOwner(String name) {
		this.name = name;
	}
	
	public String getFactionName() {
		return this.name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
	}
	
	public static class Handler implements IMessageHandler<PacketLandOwner, IMessage> {

		@Override
		public IMessage onMessage(PacketLandOwner message, MessageContext ctx) {
			if(FactionMod.proxy instanceof ClientProxy) {
				((ClientProxy)FactionMod.proxy).landOwner = message.getFactionName();
			}
			return null;
		}
		
	}

}
