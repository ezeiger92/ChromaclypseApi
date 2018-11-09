package com.chromaclypse.api;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
	
	public static void serverAddChannel(Plugin plugin, String channel) throws Exception {
		Object messenger = plugin.getServer().getMessenger();
		Method m = messenger.getClass().getDeclaredMethod("addToOutgoing", Plugin.class, String.class);
		boolean access = m.isAccessible();
		
		m.setAccessible(true);
		m.invoke(messenger, plugin, channel);
		m.setAccessible(access);
	}
	
	public static void serverRemoveChannel(Plugin plugin, String channel) throws Exception {
		Object messenger = plugin.getServer().getMessenger();
		Method m = messenger.getClass().getDeclaredMethod("removeFromOutgoing", Plugin.class, String.class);
		boolean access = m.isAccessible();
		
		m.setAccessible(true);
		m.invoke(messenger, plugin, channel);
		m.setAccessible(access);
	}
}
