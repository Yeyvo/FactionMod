package fr.mff.facmod.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.util.conversion.CustomMapping;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLLog;

public class WandWorker
{
  private final IWand wand;
  private final IPlayerShim player;
  private final IWorldShim world;
  HashSet<Point3d> allCandidates = new HashSet();
  
  public WandWorker(IWand wand, IPlayerShim player, IWorldShim world)
  {
    this.wand = wand;
    this.player = player;
    this.world = world;
  }
  
  public ItemStack getProperItemStack(IWorldShim world, IPlayerShim player, Point3d blockPos)
  {
    Block block = world.getBlock(blockPos);
    int meta = world.getMetadata(blockPos);
    ItemStack exactItemstack = new ItemStack(block, 1, meta);
    if (player.countItems(exactItemstack) > 0) {
      return exactItemstack;
    }
    return getEquivalentItemStack(blockPos);
  }
  
  public ItemStack getEquivalentItemStack(Point3d blockPos)
  {
    Block block = this.world.getBlock(blockPos);
    int meta = this.world.getMetadata(blockPos);
    
    ItemStack stack = null;
    CustomMapping customMapping = FactionMod.INSTANCE.mappingManager.getMapping(block, meta);
    if (customMapping != null)
    {
      stack = customMapping.getItems();
    }
    else if (block.canSilkHarvest(this.world.getWorld(), blockPos.toBlockPos(), block.getStateFromMeta(meta), this.player.getPlayer()))
    {
      stack = FactionMod.INSTANCE.blockCache.getStackedBlock(this.world, blockPos);
    }
    else
    {
      Item dropped = block.getItemDropped(block.getStateFromMeta(meta), new Random(), 0);
      if (dropped != null) {
        stack = new ItemStack(dropped, block.quantityDropped(block.getStateFromMeta(meta), 0, new Random()), block.damageDropped(block.getStateFromMeta(meta)));
      }
    }
    return stack;
  }
  
  private boolean shouldContinue(Point3d currentCandidate, Block targetBlock, int targetMetadata, EnumFacing facing, Block candidateSupportingBlock, int candidateSupportingMeta, AxisAlignedBB blockBB, EnumFluidLock fluidLock)
  {
    if (!this.world.blockIsAir(currentCandidate))
    {
      Block currrentCandidateBlock = this.world.getBlock(currentCandidate);
      if ((fluidLock != EnumFluidLock.IGNORE) || (currrentCandidateBlock == null) || ((!(currrentCandidateBlock instanceof IFluidBlock)) && (!(currrentCandidateBlock instanceof BlockLiquid)))) {
        return false;
      }
    }
    if (!targetBlock.equals(candidateSupportingBlock)) {
      return false;
    }
    if (targetMetadata != candidateSupportingMeta) {
      return false;
    }
    if (!targetBlock.canPlaceBlockAt(this.world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z))) {
      return false;
    }
    if (!targetBlock.canReplace(this.world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z), facing, new ItemStack(candidateSupportingBlock, 1, candidateSupportingMeta))) {
      return false;
    }
    return !this.world.entitiesInBox(blockBB);
  }
  
  public LinkedList<Point3d> getBlockPositionList(Point3d blockLookedAt, EnumFacing placeDirection, int maxBlocks, EnumLock directionLock, EnumLock faceLock, EnumFluidLock fluidLock)
  {
    LinkedList<Point3d> candidates = new LinkedList();
    LinkedList<Point3d> toPlace = new LinkedList();
    
    Block targetBlock = this.world.getBlock(blockLookedAt);
    int targetMetadata = this.world.getMetadata(blockLookedAt);
    Point3d startingPoint = blockLookedAt.move(placeDirection);
    
    int directionMaskInt = directionLock.mask;
    int faceMaskInt = faceLock.mask;
    if (((directionLock != EnumLock.HORIZONTAL) && (directionLock != EnumLock.VERTICAL)) || ((placeDirection != EnumFacing.UP) && (placeDirection != EnumFacing.DOWN) && ((directionLock != EnumLock.NORTHSOUTH) || ((placeDirection != EnumFacing.NORTH) && (placeDirection != EnumFacing.SOUTH))) && ((directionLock != EnumLock.EASTWEST) || ((placeDirection != EnumFacing.EAST) && (placeDirection != EnumFacing.WEST))))) {
      candidates.add(startingPoint);
    }
    while ((candidates.size() > 0) && (toPlace.size() < maxBlocks))
    {
      Point3d currentCandidate = (Point3d)candidates.removeFirst();
      
      Point3d supportingPoint = currentCandidate.move(placeDirection.getOpposite());
      Block candidateSupportingBlock = this.world.getBlock(supportingPoint);
      int candidateSupportingMeta = this.world.getMetadata(supportingPoint);
      AxisAlignedBB blockBB = targetBlock.getCollisionBoundingBox(this.world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z), targetBlock.getStateFromMeta(targetMetadata));
      if ((shouldContinue(currentCandidate, targetBlock, targetMetadata, placeDirection, candidateSupportingBlock, candidateSupportingMeta, blockBB, fluidLock)) && 
        (this.allCandidates.add(currentCandidate)))
      {
        toPlace.add(currentCandidate);
        switch (placeDirection)
        {
        case DOWN: 
        case UP: 
          if ((faceMaskInt & 0x2) > 0)
          {
            if ((directionMaskInt & 0x1) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.NORTH));
            }
            if ((directionMaskInt & 0x4) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.EAST));
            }
            if ((directionMaskInt & 0x1) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.SOUTH));
            }
            if ((directionMaskInt & 0x4) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.WEST));
            }
            if (((directionMaskInt & 0x1) > 0) && ((directionMaskInt & 0x4) > 0))
            {
              candidates.add(currentCandidate.move(EnumFacing.NORTH).move(EnumFacing.EAST));
              candidates.add(currentCandidate.move(EnumFacing.NORTH).move(EnumFacing.WEST));
              candidates.add(currentCandidate.move(EnumFacing.SOUTH).move(EnumFacing.EAST));
              candidates.add(currentCandidate.move(EnumFacing.SOUTH).move(EnumFacing.WEST));
            }
          }
          break;
        case NORTH: 
        case SOUTH: 
          if ((faceMaskInt & 0x1) > 0)
          {
            if ((directionMaskInt & 0x2) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.UP));
            }
            if ((directionMaskInt & 0x4) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.EAST));
            }
            if ((directionMaskInt & 0x2) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.DOWN));
            }
            if ((directionMaskInt & 0x4) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.WEST));
            }
            if (((directionMaskInt & 0x2) > 0) && ((directionMaskInt & 0x4) > 0))
            {
              candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.EAST));
              candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.WEST));
              candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.EAST));
              candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.WEST));
            }
          }
          break;
        case WEST: 
        case EAST: 
          if ((faceMaskInt & 0x4) > 0)
          {
            if ((directionMaskInt & 0x2) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.UP));
            }
            if ((directionMaskInt & 0x1) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.NORTH));
            }
            if ((directionMaskInt & 0x2) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.DOWN));
            }
            if ((directionMaskInt & 0x1) > 0) {
              candidates.add(currentCandidate.move(EnumFacing.SOUTH));
            }
            if (((directionMaskInt & 0x2) > 0) && ((directionMaskInt & 0x1) > 0))
            {
              candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.NORTH));
              candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.SOUTH));
              candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.NORTH));
              candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.SOUTH));
            }
          }
          break;
        }
      }
    }
    return toPlace;
  }
  
  public ArrayList<Point3d> placeBlocks(ItemStack wandItem, LinkedList<Point3d> blockPosList, Point3d originalBlock, ItemStack sourceItems, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    ArrayList<Point3d> placedBlocks = new ArrayList();
    for (Point3d blockPos : blockPosList)
    {
      CustomMapping mapping = FactionMod.INSTANCE.mappingManager.getMapping(this.world.getBlock(originalBlock), this.world.getMetadata(originalBlock));
      boolean blockPlaceSuccess;
      if (mapping != null) {
        blockPlaceSuccess = this.world.setBlock(blockPos, mapping.getPlaceBlock(), mapping.getPlaceMeta());
      } else {
        blockPlaceSuccess = this.world.copyBlock(originalBlock, blockPos);
      }
      if (blockPlaceSuccess)
      {
        Item itemFromBlock = Item.getItemFromBlock(this.world.getBlock(originalBlock));
        this.world.playPlaceAtBlock(blockPos, this.world.getBlock(originalBlock));
        
        placedBlocks.add(blockPos);
        if (!this.player.isCreative()) {
          this.wand.placeBlock(wandItem, this.player.getPlayer());
        }
        boolean takeFromInventory = this.player.useItem(sourceItems);
        if (!takeFromInventory)
        {
          FMLLog.info("BBW takeback: %s", new Object[] { blockPos.toString() });
          this.world.setBlockToAir(blockPos);
          placedBlocks.remove(placedBlocks.size() - 1);
        }
      }
    }
    return placedBlocks;
  }
}
