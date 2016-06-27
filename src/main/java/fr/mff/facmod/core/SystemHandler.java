package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.List;

import fr.mff.facmod.core.features.Faction;

public class SystemHandler {

	private static final List<Faction> factions = new ArrayList<Faction>();

	public static List<Faction> getAllFactions() {
		return factions;
	}

	public static boolean removeFaction(Faction faction) {
		return factions.remove(faction);
	}

	public static boolean addFaction(Faction faction) {
		return factions.add(faction);
	}

}
