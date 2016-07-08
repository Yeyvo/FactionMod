package fr.mff.facmod.wand.util.conversion;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CustomMapping
{
  private final Block lookBlock;
  private final int meta;
  private final ItemStack items;
  private final Block placeBlock;
  private final int placeMeta;
  
  public CustomMapping(Block lookBlock, int lookMeta, ItemStack items, Block placeBlock, int placeMeta)
  {
    this.lookBlock = lookBlock;
    this.meta = lookMeta;
    this.items = items;
    this.placeBlock = placeBlock;
    this.placeMeta = placeMeta;
  }
  
  public Block getLookBlock()
  {
    return this.lookBlock;
  }
  
  public int getMeta()
  {
    return this.meta;
  }
  
  public ItemStack getItems()
  {
    return this.items;
  }
  
  public Block getPlaceBlock()
  {
    return this.placeBlock;
  }
  
  public int getPlaceMeta()
  {
    return this.placeMeta;
  }
  
  public boolean equals(CustomMapping that)
  {
    return (this.lookBlock == that.lookBlock) && (this.meta == that.meta) && (this.items == that.items) && (this.placeBlock == that.placeBlock) && (this.placeMeta == that.placeMeta);
  }
}
