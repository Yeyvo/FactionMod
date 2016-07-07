package fr.mff.facmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.proxy.ClientProxy;

public class PacketRank implements IMessage {
	
	public PacketRank() {}
	
	private String rankName;
	
	public PacketRank(EnumRank rank) {
		this.rankName = rank.name();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.rankName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, rankName);
	}
	
	public static class Handler implements IMessageHandler<PacketRank, IMessage> {

		@Override
		public IMessage onMessage(PacketRank message, MessageContext ctx) {
			if(FactionMod.proxy instanceof ClientProxy) {
				((ClientProxy)FactionMod.proxy).rank = EnumRank.valueOf(message.rankName);
			}
			return null;
		}
		
	}

}
