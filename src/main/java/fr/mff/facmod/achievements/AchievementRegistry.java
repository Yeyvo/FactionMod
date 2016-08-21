package fr.mff.facmod.achievements;

import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import fr.mff.facmod.blocks.BlockRegistry;
import fr.mff.facmod.items.ItemRegistry;

public class AchievementRegistry {
	
	public static Achievement factionCreated, homeSet, lvl60reached;
	public static AchievementPage page;
	
	public static void preInit(FMLPreInitializationEvent event) {
		factionCreated = new Achievement("factionCreated", "factionCreated", 0, 0, Items.wooden_sword, null);
		homeSet = new Achievement("homeSet", "homeSet", 0, -1, BlockRegistry.homeBase, factionCreated);
		lvl60reached = new Achievement("lvl60reached", "lvl60reached", 1, -1, ItemRegistry.landMap, homeSet);
		page = new AchievementPage("Faction", factionCreated, homeSet, lvl60reached);
		AchievementPage.registerAchievementPage(page);
	}

}
