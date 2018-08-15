package com.chromaclypse.api.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * @brief     Plugin retrieval callback.
 * @author    Erik Zeiger
 * @version   1.1
 * @date      2017-
 * @copyright MIT License
 *
 */
public abstract class FuturePlugin implements Listener {
	private final String pluginName;
	
	public FuturePlugin(Plugin callingPlugin, String desiredPluginName) {
		pluginName = desiredPluginName;
		
		PluginManager manager = callingPlugin.getServer().getPluginManager();
		Plugin handle = manager.getPlugin(pluginName);
		
		if(handle != null && handle.isEnabled())
			onFindPlugin(handle);
		
		else
			manager.registerEvents(this, callingPlugin);
	}
	
	@EventHandler
	public final void onPluginLoad(PluginEnableEvent event) {
		if(event.getPlugin().getName().equals(pluginName)) {
			onFindPlugin(event.getPlugin());
			event.getHandlers().unregister(this);
		}
	}
	
	public abstract void onFindPlugin(Plugin desiredPlugin);
}
