package fr.mff.facmod.tileentities.network;

import fr.mff.facmod.core.extendedProperties.ExtendedPropertieFaction;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSetFaction implements IMessage {
	
	public String facName;
	
	public PacketSetFaction() { }
	
	public PacketSetFaction(String facName) {
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
	
	public static class Handler implements IMessageHandler<PacketSetFaction, IMessage> {
		
		@Override
		public IMessage onMessage(PacketSetFaction message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				ExtendedPropertieFaction prop = ExtendedPropertieFaction.get(Minecraft.getMinecraft().thePlayer);
				prop.factionName = message.facName;
			} else {
				ExtendedPropertieFaction prop = ExtendedPropertieFaction.get(ctx.getServerHandler().playerEntity);
				prop.factionName = message.facName;
			}
			return null;
		}
		
	}

}
