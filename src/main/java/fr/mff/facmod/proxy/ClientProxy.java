package fr.mff.facmod.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.handlers.ClientEventHandler;

public class ClientProxy extends CommonProxy {
	
	public String factionName = "";
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		registerRenders();
	}
	
	/**
	 * Register all renders (blocks, items, ect ...)
	 */
	private void registerRenders() {
		registerBlockTexture(BlockRegistry.homeBase, "homeBase");
	}
	
	@Override
	public void registerItemTexture(Item item, int metadata, String name)
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		mesher.register(item, metadata, new ModelResourceLocation(FactionMod.MODID + ":" + name, "inventory"));
	}

	@Override
	public void registerItemTexture(Item item, String name)
	{
		registerItemTexture(item, 0, name);
	}

	@Override
	public void registerBlockTexture(Block block, int metadata, String name)
	{
		registerItemTexture(Item.getItemFromBlock(block), metadata, name);
	}

	@Override
	public void registerBlockTexture(Block block, String name)
	{
		registerBlockTexture(block, 0, name);
	}

}
