package fr.mff.facmod.network;

import fr.mff.facmod.client.gui.GuiLandMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenMap implements IMessage {

	private String[] names;

	public PacketOpenMap() { }

	public PacketOpenMap(String[] ownersNames) {
		this.names = ownersNames;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		this.names = new String[size];
		for(int i = 0; i < this.names.length; i++) {
			this.names[i] = ByteBufUtils.readUTF8String(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(names.length);
		for(int i = 0; i < this.names.length; i++) {
			ByteBufUtils.writeUTF8String(buf, names[i]);
		}
	}

	public String[] getFactionNames() {
		return this.names;
	}

	public static class Handler implements IMessageHandler<PacketOpenMap, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketOpenMap message, MessageContext ctx) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiLandMap(message.names));
			return null;
		}

	}

}
