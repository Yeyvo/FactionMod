package fr.mff.facmod.core;

import java.util.Date;
import java.util.HashMap;

public class TimesHome {
	
	private static HashMap<String, Long> lastOpening = new HashMap<String, Long>();
	
	public static void setLastOpening(String fac, long date){
        lastOpening.put(fac, date);
    }
	
	public static long getLastOpening(String name){
        Long time = lastOpening.get(name);
        return time == null ? new Date().getTime() : time;
    }	
}
