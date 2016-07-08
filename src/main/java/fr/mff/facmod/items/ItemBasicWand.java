package fr.mff.facmod.items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.wand.util.BasicPlayerShim;
import fr.mff.facmod.wand.util.BasicWorldShim;
import fr.mff.facmod.wand.util.CreativePlayerShim;
import fr.mff.facmod.wand.util.EnumFluidLock;
import fr.mff.facmod.wand.util.EnumLock;
import fr.mff.facmod.wand.util.IPlayerShim;
import fr.mff.facmod.wand.util.IWand;
import fr.mff.facmod.wand.util.IWandItem;
import fr.mff.facmod.wand.util.IWorldShim;
import fr.mff.facmod.wand.util.Point3d;
import fr.mff.facmod.wand.util.WandWorker;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class ItemBasicWand extends Item implements IWandItem {
	public IWand wand;

	public ItemBasicWand() {
		setCreativeTab(FactionMod.factionTabs);
		setMaxStackSize(1);
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (this.wand == null) {
			return false;
		}
		if (itemstack == null) {
			return false;
		}
		if (!world.isRemote) {
			IPlayerShim playerShim = new BasicPlayerShim(player);
			if (player.capabilities.isCreativeMode) {
				playerShim = new CreativePlayerShim(player);
			}
			IWorldShim worldShim = new BasicWorldShim(world);

			WandWorker worker = new WandWorker(this.wand, playerShim, worldShim);

			Point3d clickedPos = new Point3d(pos.getX(), pos.getY(), pos.getZ());

			ItemStack sourceItems = worker.getProperItemStack(worldShim, playerShim, clickedPos);
			if ((sourceItems != null) && ((sourceItems.getItem() instanceof ItemBlock))) {
				int numBlocks = Math.min(this.wand.getMaxBlocks(itemstack), playerShim.countItems(sourceItems));

				LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, side, numBlocks,
						getMode(itemstack), getFaceLock(itemstack), getFluidMode(itemstack));

				ArrayList<Point3d> placedBlocks = worker.placeBlocks(itemstack, blocks, clickedPos, sourceItems, side,
						hitX, hitY, hitZ);
				if (placedBlocks.size() > 0) {
					int[] placedIntArray = new int[placedBlocks.size() * 3];
					for (int i = 0; i < placedBlocks.size(); i++) {
						Point3d currentPoint = (Point3d) placedBlocks.get(i);
						placedIntArray[(i * 3)] = (int) currentPoint.x;
						placedIntArray[(i * 3 + 1)] = (int) currentPoint.y;
						placedIntArray[(i * 3 + 2)] = (int) currentPoint.z;
					}
					NBTTagCompound itemNBT = itemstack.hasTagCompound() ? itemstack.getTagCompound()
							: new NBTTagCompound();
					NBTTagCompound facmodCompond = new NBTTagCompound();
					if (itemNBT.hasKey("facmod", 10)) {
						facmodCompond = itemNBT.getCompoundTag("facmod");
					}
					if (!facmodCompond.hasKey("mask", 2)) {
						facmodCompond.setShort("mask", (short) getDefaultMode().mask);
					}
					if (!facmodCompond.hasKey("fluidmask", 2)) {
						facmodCompond.setShort("fluidmask", (short) getDefaultFluidMode().mask);
					}
					facmodCompond.setIntArray("lastPlaced", placedIntArray);
					facmodCompond.setString("lastBlock",
							((ResourceLocation) Item.itemRegistry.getNameForObject(sourceItems.getItem())).toString());
					facmodCompond.setInteger("lastBlockMeta", sourceItems.getItemDamage());
					facmodCompond.setInteger("lastPerBlock", sourceItems.stackSize);
					itemstack.setTagInfo("facmod", facmodCompond);
				}
			}
		}
		return true;
	}

	public void addInformation(ItemStack itemstack, EntityPlayer player, List lines, boolean extraInfo) {
		EnumLock mode = getMode(itemstack);
		switch (mode) {
		case NORTHSOUTH:
			lines.add(StatCollector.translateToLocal("mode.northsouth"));
			break;
		case VERTICAL:
			lines.add(StatCollector.translateToLocal("mode.vertical"));
			break;
		case VERTICALEASTWEST:
			lines.add(StatCollector.translateToLocal("mode.verticaleastwest"));
			break;
		case EASTWEST:
			lines.add(StatCollector.translateToLocal("mode.eastwest"));
			break;
		case HORIZONTAL:
			lines.add(StatCollector.translateToLocal("mode.horizontal"));
			break;
		case VERTICALNORTHSOUTH:
			lines.add(StatCollector.translateToLocal("mode.verticalnorthsouth"));
			break;
		case NOLOCK:
			lines.add(StatCollector.translateToLocal("mode.nolock"));
		}
		EnumFluidLock fluidMode = getFluidMode(itemstack);
		switch (fluidMode) {
		case STOPAT:
			lines.add(StatCollector.translateToLocal("fluidmode.stopat"));
			break;
		case IGNORE:
			lines.add(StatCollector.translateToLocal("fluidmode.ignore"));
		}
		if ((!itemstack.isItemStackDamageable()) || (!itemstack.isItemDamaged())) {
			lines.add(StatCollector.translateToLocalFormatted("bloc.maxblocks",
					new Object[] { Integer.valueOf(this.wand.getMaxBlocks(itemstack)) }));
		}
	}

	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos,
			EntityLivingBase playerIn) {
		stack.damageItem(2, playerIn);
		return true;
	}

	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
		p_77644_1_.damageItem(2, p_77644_3_);
		return true;
	}

	public void setMode(ItemStack item, EnumLock mode) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		if (item.hasTagCompound()) {
			tagCompound = item.getTagCompound();
		}
		NBTTagCompound facmodCompond = new NBTTagCompound();
		if (tagCompound.hasKey("facmod", 10)) {
			facmodCompond = tagCompound.getCompoundTag("facmod");
		}
		short shortMask = (short) (mode.mask & 0x7);
		facmodCompond.setShort("mask", shortMask);
		tagCompound.setTag("facmod", facmodCompond);
		item.setTagCompound(tagCompound);
	}

	public EnumLock getMode(ItemStack item) {
		if ((item != null) && (item.getItem() != null) && ((item.getItem() instanceof IWandItem))) {
			NBTTagCompound itemBaseNBT = item.getTagCompound();
			if ((itemBaseNBT != null) && (itemBaseNBT.hasKey("facmod", 10))) {
				NBTTagCompound itemNBT = itemBaseNBT.getCompoundTag("facmod");
				int mask = itemNBT.hasKey("mask", 2) ? itemNBT.getShort("mask") : EnumLock.NOLOCK.mask;
				return EnumLock.fromMask(mask);
			}
		}
		return getDefaultMode();
	}

	public void setFluidMode(ItemStack item, EnumFluidLock mode) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		if (item.hasTagCompound()) {
			tagCompound = item.getTagCompound();
		}
		NBTTagCompound facmodCompond = new NBTTagCompound();
		if (tagCompound.hasKey("facmod", 10)) {
			facmodCompond = tagCompound.getCompoundTag("facmod");
		}
		short shortMask = (short) (mode.mask & 0x7);
		facmodCompond.setShort("fluidmask", shortMask);
		tagCompound.setTag("facmod", facmodCompond);
		item.setTagCompound(tagCompound);
	}

	public EnumFluidLock getFluidMode(ItemStack item) {
		if ((item != null) && (item.getItem() != null) && ((item.getItem() instanceof IWandItem))) {
			NBTTagCompound itemBaseNBT = item.getTagCompound();
			if ((itemBaseNBT != null) && (itemBaseNBT.hasKey("facmod", 10))) {
				NBTTagCompound itemNBT = itemBaseNBT.getCompoundTag("facmod");
				int mask = itemNBT.hasKey("fluidmask", 2) ? itemNBT.getShort("fluidmask") : EnumFluidLock.STOPAT.mask;
				return EnumFluidLock.fromMask(mask);
			}
		}
		return getDefaultFluidMode();
	}

	public IWand getWand() {
		return this.wand;
	}

	public EnumLock getDefaultMode() {
		return EnumLock.NOLOCK;
	}

	public EnumFluidLock getDefaultFluidMode() {
		return EnumFluidLock.STOPAT;
	}
}
