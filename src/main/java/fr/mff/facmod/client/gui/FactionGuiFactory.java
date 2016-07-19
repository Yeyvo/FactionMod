package fr.mff.facmod.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import fr.mff.facmod.FactionMod;
import fr.mff.facmod.config.ConfigFaction;

public class FactionGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiConfigFaction.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
	
	public static class GuiConfigFaction extends GuiConfig {

		public GuiConfigFaction(GuiScreen parent) {
			super(parent, getConfigElements(), FactionMod.MODID, false, false, "");
		}
		
		public static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			list.add(new DummyCategoryElement(Configuration.CATEGORY_GENERAL, "config.general", new ConfigElement(ConfigFaction.cfg.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements()));
			list.add(new DummyCategoryElement(Configuration.CATEGORY_CLIENT, "config.client", new ConfigElement(ConfigFaction.cfg.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements()));
			list.add(new DummyCategoryElement("addons", "config.addons", new ConfigElement(ConfigFaction.cfg.getCategory("addons")).getChildElements()).setRequiresWorldRestart(true));
			return list;
		}

	}

}
