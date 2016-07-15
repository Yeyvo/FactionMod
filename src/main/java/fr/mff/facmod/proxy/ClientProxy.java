package fr.mff.facmod.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.core.EnumRank;
import fr.mff.facmod.entity.EntityDynamite;
import fr.mff.facmod.handlers.ClientEventHandler;
import fr.mff.facmod.items.ItemRegistry;
import fr.mff.facmod.renderer.RenderDynamite;

public class ClientProxy extends CommonProxy {

	
	/**
	 * @author BrokenSwing
	 */
	public String factionName = "";
	public EnumRank rank = EnumRank.WITHOUT_FACTION;
	public String landOwner = "";

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

		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		registerBlockTexture(BlockRegistry.homeBase, "homeBase");
		registerBlockTexture(BlockRegistry.xrayBlock, "xrayBlock");


		registerItemTexture(ItemRegistry.landMap, "landMap");
		registerItemTexture(ItemRegistry.dynamite, "dynamite");
		registerItemTexture(ItemRegistry.homeFinder, "homeFinder");
		registerItemTexture(ItemRegistry.chestWatcher, "chestWatcher");


		Render(EntityDynamite.class, new RenderDynamite(renderManager, renderItem));

	}
	public void Render(Class<? extends Entity> par1, Render par2)
	{
		RenderingRegistry.registerEntityRenderingHandler(par1, par2);
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
