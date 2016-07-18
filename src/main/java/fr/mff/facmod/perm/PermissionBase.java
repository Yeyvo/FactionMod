package fr.mff.facmod.perm;

import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.mff.facmod.FactionMod;

public class PermissionBase {

	protected HashMap<String, PermissionBase> directories = Maps.newHashMap();
	protected HashMap<String, PermissionBase> files = Maps.newHashMap();
	protected String name;
	protected PermissionBase parent;

	public PermissionBase(String name, PermissionBase parent) {
		this.name = name;
		this.parent = parent;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public String getPath() {
		if(this.isRoot() || parent.isRoot()) {
			return this.name;
		}
		if(parent.isRoot()) {
			return this.name;
		}
		return parent.getPath() + "." + this.name;
	}

	public Set<PermissionBase> get(String name) {
		Set<PermissionBase> bases = Sets.newHashSet();
		if(name.matches("^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*)|(([a-zA-Z0-9]+\\.)*\\*)$")) {
			String[] path = name.split("\\.", 2);
			if(path[0].equals("*")) {
				for(PermissionBase directory : directories.values()) {
					bases.addAll(directory.get("*"));
				}
				for(PermissionBase file : files.values()) {
					bases.add(file);
				}
			} else {
				if(path.length == 1) {
					PermissionBase file = files.get(path[0]);
					if(file != null) {
						bases.add(file);
					}
				} else {
					PermissionBase dir = directories.get(path[0]);
					if(dir != null) {
						bases.addAll(dir.get(path[1]));
					}
				}
			}
		} else {
			FactionMod.logger.warn("Tried to get permission \"" + name + "\" but the format is incorrect");
		}
		return bases;
	}

	public void add(String name) {
		if(name.matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$")) {
			String[] path = name.split("\\.", 2);
			if(path.length == 1) {
				files.put(path[0], new PermissionBase(path[0], this));
				FactionMod.logger.info("Added permission file \"" + path[0] + "\" to directory \"" + this.getPath() + "\"");
			} else {
				PermissionBase dir = directories.get(path[0]);
				if(dir == null) {
					dir = new PermissionBase(path[0], this);
					directories.put(path[0], dir);
				}
				dir.add(path[1]);
			}
		} else {
			FactionMod.logger.warn("Tried to add permission \"" + name + "\" but the format is incorrect");
		}
	}

}
