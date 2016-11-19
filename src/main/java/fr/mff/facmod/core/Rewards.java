package fr.mff.facmod.core;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;

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
	
    public static void writeToNBT(NBTTagCompound compound) {
        NBTTagList RewardList = new NBTTagList();
 
        Iterator<Entry<String, Integer>> iterator = lastOpening.entrySet().iterator();
        while (iterator.hasNext()) {
            NBTTagCompound RewardTag = new NBTTagCompound();
            Entry<String, Integer> entry = iterator.next();
            RewardTag.setString("faction", entry.getKey());
            RewardTag.setInteger("time", entry.getValue());
            RewardList.appendTag(RewardTag);
        }
 
        compound.setTag("reward", RewardList);
    }
 
    public static void readFromNBT(NBTTagCompound compound) {
        Rewards.clearRewards();
        NBTTagList RewardList = (NBTTagList) compound.getTag("lastOpening");

        for (int i = 0; i < RewardList.tagCount(); i++) {
            NBTTagCompound homeTag = RewardList.getCompoundTagAt(i);
            int day = homeTag.getInteger("time");
            lastOpening.put(homeTag.getString("faction"), day);
            FactionSaver.save();
        }
    }
    
    public static void clearRewards() {
    	lastOpening.clear();
        }
}
