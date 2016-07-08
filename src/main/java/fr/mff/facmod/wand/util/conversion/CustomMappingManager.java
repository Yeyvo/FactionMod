package fr.mff.facmod.wand.util.conversion;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

public class CustomMappingManager
{
  ArrayList<CustomMapping> mappings;
  
  public CustomMappingManager()
  {
    this.mappings = new ArrayList();
  }
  
  public void loadConfig(String configString)
  {
    if (configString.trim().isEmpty()) {
      return;
    }
    String[] split = configString.trim().split(",");
    for (String part : split)
    {
      CustomMapping customMapping = parseMappingString(part);
      if (customMapping != null) {
        this.mappings.add(customMapping);
      }
    }
  }
  
  public CustomMapping parseMappingString(String mappingString)
  {
    String REGEX = "^(\\w[^:]+:[^/]+)/(\\d+)=>(\\d)+\\*(\\w[^:]+:[^/]+)/(\\d+)=>(\\w[^:]+:[^/]+)/(\\d+)$";
    Pattern p = Pattern.compile("^(\\w[^:]+:[^/]+)/(\\d+)=>(\\d)+\\*(\\w[^:]+:[^/]+)/(\\d+)=>(\\w[^:]+:[^/]+)/(\\d+)$");
    Matcher m = p.matcher(mappingString);
    if ((!mappingString.isEmpty()) && (m.matches()))
    {
      Block sourceBlock = Block.getBlockFromName(m.group(1));
      int sourceMeta = Integer.parseInt(m.group(2));
      int itemCount = Integer.parseInt(m.group(3));
      
      String itemString = m.group(4);
      Item itemItem = GameRegistry.findItem(itemString.split(":", 2)[0], itemString.split(":", 2)[1]);
      
      int itemMeta = Integer.parseInt(m.group(5));
      Block targetBlock = Block.getBlockFromName(m.group(6));
      int targetMeta = Integer.parseInt(m.group(7));
      if ((sourceBlock != null) && (itemItem != null) && (targetBlock != null))
      {
        ItemStack itemItemstack = new ItemStack(itemItem, itemCount, itemMeta);
        CustomMapping newMapping = new CustomMapping(sourceBlock, sourceMeta, itemItemstack, targetBlock, targetMeta);
        return newMapping;
      }
    }
    return null;
  }
  
  public CustomMapping getMapping(Block block, int meta)
  {
    for (CustomMapping mapping : this.mappings) {
      if ((mapping.getLookBlock() == block) && (mapping.getMeta() == meta)) {
        return mapping;
      }
    }
    return null;
  }
  
  public void setMapping(CustomMapping newMapping)
  {
    for (int i = 0; i < this.mappings.size(); i++)
    {
      CustomMapping current = (CustomMapping)this.mappings.get(i);
      if ((current.getLookBlock() == newMapping.getLookBlock()) && (current.getMeta() == newMapping.getMeta()))
      {
        if (current.equals(newMapping)) {
          return;
        }
        this.mappings.set(i, newMapping);
        return;
      }
    }
    this.mappings.add(newMapping);
  }
}
