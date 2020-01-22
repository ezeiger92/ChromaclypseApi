package com.chromaclypse.api.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.chromaclypse.api.command.Context;
import com.chromaclypse.api.messages.Text;

public class CommandBase {
	private final CommandNode root = new CommandNode();
	
	CommandNode getRoot() {
		return root;
	}
	
	public OperationBuilder with() {
		return new OperationBuilder(this);
	}
	
	public CommandBase calls(Function<Context, Boolean> callback) {
		return with().calls(callback);
	}
	
	public TabExecutor getCommand() {
		return new Handler(root);
	}
	
	private static class Handler implements TabExecutor {
		private final CommandNode tree;
		
		private Handler(CommandNode tree) {
			this.tree = tree;
		}
		
		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			return tree.tabCompletion(new Context(sender, command, alias, args));
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			try {
				return tree.execute(new Context(sender, command, label, args));
			}
			catch(Throwable e) {
				sender.sendMessage(Text.format().colorize("&cError: " + e.getMessage()));
				return true;
			}
		}
	}
	

	public static List<String> onlinePlayers(Context context) {
		List<String> players = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			players.add(p.getName());
		}
		return players;
	}
	
	public static boolean pluginVersion(Context context) {
		if(context.Command() instanceof PluginCommand) {
			PluginCommand command = (PluginCommand) context.Command();
			
			String name = command.getPlugin().getName();
			String pluginVersion = command.getPlugin().getDescription().getVersion();
			String apiVersion = command.getPlugin().getDescription().getAPIVersion();
			
			String apiMessage = "";
			if(apiVersion != null && apiVersion.length() > 0) {
				apiMessage = String.format(" (using API version: %s)", apiVersion);
			}
			
			String message = String.format("%s version: %s%s", name, pluginVersion, apiMessage);
			
			context.Sender().sendMessage(Text.format().colorize(message));
		}
		else {
			context.Sender().sendMessage(Text.format().colorize("&cError: This is a builtin command (how did we get here?)"));
		}
		
		return true;
	}
	
	public static boolean unknownCommand(Context context) {
		String current = String.join(" ", context.Args()).trim();
		String message = "&7/" + context.Alias() + " " + current + "&c&o<--[HERE]";
		
		context.Sender().sendMessage(Text.format().colorize("&cUnknown command"));
		context.Sender().sendMessage(Text.format().colorize(message));
		return true;
	}
}