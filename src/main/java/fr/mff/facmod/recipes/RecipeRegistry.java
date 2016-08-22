package fr.mff.facmod.recipes;

import fr.mff.facmod.items.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *	Used to store registry all recipes with GameRegistry
 */
public class RecipeRegistry {
	
	public static void init(FMLInitializationEvent event) {
		GameRegistry.addShapedRecipe(new ItemStack(ItemRegistry.dynamite, 2), "F", "T", "T", 'F', Items.string, 'T', Blocks.tnt);
	}

}
