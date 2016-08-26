package fr.mff.facmod.core;

import java.util.Calendar;
import java.util.HashMap;

public class Rewards {
	
	//TODO Save it in World's NBT Tags
	private static HashMap<String, Integer> lastOpening = new HashMap<String, Integer>();
	
	public static void setLastOpening(String fac, int day){
        lastOpening.put(fac, day);
    }
	
	public static int getLastOpening(String name){
        Integer day = lastOpening.get(name);
        return day == null ? Calendar.getInstance().get(Calendar.DAY_OF_YEAR) : day;
    }	
}
