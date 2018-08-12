package com.chromaclypse.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class Reflect {
	private Reflect() {
	}

	private static final Class<?> serverClass = Bukkit.getServer().getClass();
	private static final String CBS = serverClass.getName().replaceFirst("[^.]+$", "");
	private static final String NMS;
	
	static {
		String nms = null;

		try {
			nms = serverClass.getMethod("getServer").getReturnType().getName().replaceFirst("[^.]+$", "");
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
		}

		NMS = nms;
	}
	
	public static Class<?> NMS(String className) throws ClassNotFoundException {
		return Class.forName(NMS + className);
	}
	
	public static Class<?> CBS(String className) throws ClassNotFoundException {
		return Class.forName(CBS + className);
	}

	public static void playerAddChannel(Player p, String s) throws Exception {
		CBS("entity.CraftPlayer").getMethod("addChannel", String.class).invoke(p, s);
	}

	public static void playerRemoveChannel(Player p, String s) throws Exception {
		CBS("entity.CraftPlayer").getMethod("removeChannel", String.class).invoke(p, s);
	}
}
