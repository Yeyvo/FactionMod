package fr.mff.facmod.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import fr.mff.facmod.handlers.CommonEventHandler;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event)
    {

    }

    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }
    
    public void registerItemTexture(Item item, int metadata, String name){}

    public void registerItemTexture(Item item, String name){}

    public void registerBlockTexture(Block block, int metadata, String name){}

    public void registerBlockTexture(Block block, String name){}

}
