package fr.mff.facmod.commands;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

import com.google.common.collect.Lists;

import fr.mff.facmod.core.EnumResult;
import fr.mff.facmod.core.Lands;

public class CommandZone implements ICommand {

	private static final List<String> zoneArgs = Lists.newArrayList();
	static {
		zoneArgs.add("safe");
		zoneArgs.add("war");
		zoneArgs.add("remove");
	}

	@Override
	public String getCommandName() {
		return "zone";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/zone <safe | war | remove>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)sender;
			if(args.length != 1) {
				throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
			}
			if(args[0].equalsIgnoreCase("safe")) {
				EnumResult result = Lands.setSafeZone(player);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			else if(args[0].equalsIgnoreCase("war")) {
				EnumResult result = Lands.setWarZone(player);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}

			else if(args[0].equalsIgnoreCase("remove")) {
				EnumResult result = Lands.removeZone(player);
				player.addChatComponentMessage(new ChatComponentTranslation(result.getLanguageKey(), result.getInformations()));
			}
		}
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public List<String> getCommandAliases() {
		return Lists.newArrayList();
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		List<String> tab = Lists.newArrayList();
		if(args.length == 1) {
			for(String s : zoneArgs) {
				if(s.startsWith(args[0])) {
					tab.add(s);
				}
			}
		}
		return tab;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
