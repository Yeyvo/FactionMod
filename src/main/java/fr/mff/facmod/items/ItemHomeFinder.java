package fr.mff.facmod.items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.core.Homes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemHomeFinder extends Item {

	public ItemHomeFinder() {
		this.setMaxStackSize(1);
		this.setMaxDamage(1);
		this.setCreativeTab(FactionMod.factionTabs);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
		if (player.inventory.consumeInventoryItem(this)) {
			if (!worldIn.isRemote) {
				Object[] crunchifyKeys = Homes.getHomes().values().toArray();
				Object key = crunchifyKeys[new Random().nextInt(crunchifyKeys.length)];
				String fac = key.toString().replace("{", "").replace("}", "").replace("=", " = ").replace("BlockPos", "");
				player.addChatComponentMessage(new ChatComponentTranslation("msg.faction.coordo.home", new Object[0]));
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + fac));
			}
		}
		return super.onItemRightClick(itemStackIn, worldIn, player);
	}
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}
