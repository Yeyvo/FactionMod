package fr.mff.facmod.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import fr.mff.facmod.FactionMod;


public class ConfigFaction {

	public static Configuration cfg;

	public static int TP_DELAY = 10;
	public static boolean FACTION_OVERLAY = true;
	public static boolean RANK_OVERLAY = true;
	public static boolean LAND_OWNER_OVERLAY = true;
	public static boolean POWER_OVERLAY = true;
	
	public static int POWER_PER_PLAYER = 5;
	public static int POWER_LOST_ON_DEATH = 1;
	public static int POWER_NEEDED_FOR_CLAIM = 2;
	public static int POWER_GIVEN_FREQUENTLY = 1;
	public static int POWER_GIVING_FREQUENCY = 5; //In minute
	
	public static boolean ENABLE_PERMISSION = true;

	public static void preInit(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();

		syncConfig();
	}


	public static void onConfigChanged(OnConfigChangedEvent event) {
		if(event.modID.equals(FactionMod.MODID)) {
			syncConfig();
		}
	}
	
	public static void syncConfig() {
		
		Property prop;
		List<String> propOrder = new ArrayList<String>();
		
		prop = cfg.get(Configuration.CATEGORY_GENERAL, "tpDelay", 10);
		prop.comment = "Delay before being teleported";
		prop.setLanguageKey("config.general.tpDelay");
		prop.setMaxValue(Integer.MAX_VALUE);
		prop.setMinValue(1);
		TP_DELAY = prop.getInt(10);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_GENERAL, "powerPerPlayer", 5);
		prop.comment = "The maximum power a player can hold";
		prop.setLanguageKey("config.general.powerPerPlayer");
		prop.setMaxValue(Integer.MAX_VALUE);
		prop.setMinValue(1);
		POWER_PER_PLAYER = prop.getInt(5);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_GENERAL, "powerNeededForClaim", 2);
		prop.comment = "The power needed to claim a land";
		prop.setLanguageKey("config.general.powerNeededForClaim");
		prop.setMaxValue(Integer.MAX_VALUE);
		prop.setMinValue(1);
		POWER_NEEDED_FOR_CLAIM = prop.getInt(2);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_GENERAL, "powerLostOnDeath", 2);
		prop.comment = "The power a player lost when is die";
		prop.setLanguageKey("config.general.powerLostOnDeath");
		prop.setMaxValue(Integer.MAX_VALUE);
		prop.setMinValue(1);
		POWER_LOST_ON_DEATH = prop.getInt(2);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_GENERAL, "powerGivenFrequently", 1);
		prop.comment = "The power a player get when the server give him frequently some power";
		prop.setLanguageKey("config.general.powerGivenFrequently");
		prop.setMaxValue(Integer.MAX_VALUE);
		prop.setMinValue(1);
		POWER_GIVEN_FREQUENTLY = prop.getInt(1);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_GENERAL, "powerGivingFrequency", 1);
		prop.comment = "The timer between to giving of power to the player from the server (in minute)";
		prop.setLanguageKey("config.general.powerGivingFrequency");
		prop.setMaxValue(Integer.MAX_VALUE / 20 / 60);
		prop.setMinValue(1);
		POWER_GIVING_FREQUENCY = prop.getInt(1);
		propOrder.add(prop.getName());
		
		cfg.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, propOrder);
		
		
		propOrder = new ArrayList<String>();
		
		prop = cfg.get(Configuration.CATEGORY_CLIENT, "factionOverlay", true);
		prop.comment = "Set to false to disable faction's name displaying";
		prop.setLanguageKey("config.client.factionOverlay");
		FACTION_OVERLAY = prop.getBoolean(true);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_CLIENT, "rankOverlay", true);
		prop.comment = "Set to false to disable rank's displaying";
		prop.setLanguageKey("config.client.rankOverlay");
		RANK_OVERLAY = prop.getBoolean(true);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_CLIENT, "landOwnerOverlay", true);
		prop.comment = "Set to false to disable land's owner's name displaying";
		prop.setLanguageKey("config.client.landOwnerOverlay");
		LAND_OWNER_OVERLAY = prop.getBoolean(true);
		propOrder.add(prop.getName());
		
		prop = cfg.get(Configuration.CATEGORY_CLIENT, "powerOverlay", true);
		prop.comment = "Set to false to disable power level displaying";
		prop.setLanguageKey("config.client.powerOverlay");
		LAND_OWNER_OVERLAY = prop.getBoolean(true);
		propOrder.add(prop.getName());
		
		cfg.setCategoryPropertyOrder(Configuration.CATEGORY_CLIENT, propOrder);
		
		propOrder = new ArrayList<String>();
		
		prop = cfg.get("addons", "enablePermission", true);
		prop.comment = "Set to false to disable permission addon";
		prop.setLanguageKey("config.addons.permission.enable");
		prop.setRequiresWorldRestart(true);
		ENABLE_PERMISSION = prop.getBoolean(true);
		propOrder.add(prop.getName());
		
		cfg.setCategoryPropertyOrder("addons", propOrder);

		if(cfg.hasChanged()) {
			cfg.save();
		}
	}

}
