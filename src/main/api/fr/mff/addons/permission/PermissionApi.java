package fr.mff.addons.permission;

import java.lang.reflect.Field;

public enum PermissionApi {
	
	;
	
	private static final String API_CLASS_PATH = "fr.mff.facmod.perm.PermissionManager";
	private static final String INSTANCE_FIELD = "API";
	
	private static IPermissionManager API;
	
	
	static {
		try {
			final Class<?> managerClass = Class.forName(API_CLASS_PATH);
			final Field instanceField = managerClass.getDeclaredField(INSTANCE_FIELD);
			API = (IPermissionManager)(instanceField.get(managerClass));
		} catch (final ClassNotFoundException e) {
			
		} catch (final NoSuchFieldException e) {
			
		} catch (final IllegalAccessException e) {
			
		}
	}
	
	public static IPermissionManager manager() {
		return API;
	}

}
