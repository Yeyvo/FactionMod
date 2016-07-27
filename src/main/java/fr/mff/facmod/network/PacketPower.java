package fr.mff.facmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.proxy.ClientProxy;

public class PacketPower implements IMessage {
	
	public PacketPower() {}
	
	private String powerLevel;
	
	public PacketPower(int powerLevel) {
		this.powerLevel = powerLevel + "/" + ConfigFaction.POWER_PER_PLAYER;
	}
	
	public String getPowerLevel() {
		return this.powerLevel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.powerLevel = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, powerLevel);
	}
	
	public static class Handler implements IMessageHandler<PacketPower, IMessage> {

		@Override
		public IMessage onMessage(PacketPower message, MessageContext ctx) {
			((ClientProxy)FactionMod.proxy).powerLevel = message.getPowerLevel();
			return null;
		}
		
	}

}
