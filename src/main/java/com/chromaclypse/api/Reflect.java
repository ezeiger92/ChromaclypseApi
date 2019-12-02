package com.chromaclypse.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class Reflect {
	private Reflect() {
	}

	private static final Class<?> serverClass = Bukkit.getServer().getClass();
	private static final String CBS_PATH = pathOf(serverClass);
	private static final String NMS_PATH;
	
	static {
		String path = null;

		try {
			path = pathOf(serverClass.getMethod("getServer").getReturnType());
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
		}

		NMS_PATH = path;
	}
	
	private static String pathOf(Class<?> clazz) {
		return clazz.getPackage().getName() + ".";
	}
	
	public static Class<?> NMS(String className) throws ClassNotFoundException {
		return Class.forName(NMS_PATH + className);
	}
	
	public static Class<?> CBS(String className) throws ClassNotFoundException {
		return Class.forName(CBS_PATH + className);
	}

	public static void playerAddChannel(Player p, String s) throws Exception {
		CBS("entity.CraftPlayer").getMethod("addChannel", String.class).invoke(p, s);
	}

	public static void playerRemoveChannel(Player p, String s) throws Exception {
		CBS("entity.CraftPlayer").getMethod("removeChannel", String.class).invoke(p, s);
	}
}
