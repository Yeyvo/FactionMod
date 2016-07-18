package fr.mff.addons.permission;

import java.util.UUID;

import net.minecraft.entity.Entity;

public interface IPermissionManager {
	
	/**
	 * Command's permission are automaticaly registered with the name "command.commandName"
	 * @param permission
	 */
	public void registerPermission(String permission);
	
	public boolean hasPlayerPermission(String name, String permission);
	
	public boolean hasEntityPermission(UUID uuid, String permission);
	
	public boolean hasEntityPermission(Entity entity, String permission);

}
