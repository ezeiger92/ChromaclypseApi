package com.chromaclypse.api.messages;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public interface Template {
	String[] format(Plugin plugin, String message, String... extra);
	
	public static class Default implements Template {
		
		private final ChatColor prefix;
		private final ChatColor primary;
		private final ChatColor highlight;

		public Default(ChatColor prefix, ChatColor primary, ChatColor highlight) {
			this.prefix = prefix;
			this.primary = primary;
			this.highlight = highlight;
		}
		
		@Override
		public String[] format(Plugin plugin, String message, String... extra) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
