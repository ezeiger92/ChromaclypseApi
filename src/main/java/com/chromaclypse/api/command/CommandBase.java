package com.chromaclypse.api.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.chromaclypse.api.command.Context;
import com.chromaclypse.api.messages.Text;

public class CommandBase {
	private static final String DYNAMIC_FILL = "<dynamic>";
	
	private CommandNode root = new CommandNode(0);
	
	public OperationBuilder with() {
		return new OperationBuilder();
	}
	
	public CommandBase calls(Function<Context, Boolean> callback) {
		return with().calls(callback);
	}
	
	public TabExecutor getCommand() {
		return new Handler(root);
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
	
	public static class CommandNode {
		private final int depth;
		private String permission = "*";
		private Map<String, CommandNode> children = new HashMap<>();
		private Function<Context, Boolean> operation = null;
		private Function<Context, List<String>> suggestions = null;
		
		private CommandNode(int depth) {
			this.depth = depth;
		}
		
		public List<String> tabCompletion(Context context) {
			String arg = context.GetArg(depth);
			
			if(context.Args().length > depth + 1) {
				CommandNode child = children.get(arg);
				if(child != null) {
					return child.tabCompletion(context);
				}
				
				child = children.get(DYNAMIC_FILL);
				if(child != null) {
					return child.tabCompletion(context);
				}
			}
			
			CommandSender sender = context.Sender();
			
			List<String> result = children.entrySet().stream()
					.filter(e -> sender.hasPermission(e.getValue().permission))
					.map(e -> e.getKey())
					.collect(Collectors.toCollection(ArrayList::new));
			
			if(suggestions != null) {
				result.addAll(suggestions.apply(context));
			}
			
			return result.stream()
					.filter(s -> s.regionMatches(true, 0, arg, 0, arg.length()))
					.filter(s -> !s.equals(DYNAMIC_FILL))
					.sorted(String.CASE_INSENSITIVE_ORDER)
					.collect(Collectors.toList());
		}
		
		public boolean execute(Context context) {
			CommandSender sender = context.Sender();
			
			if("*" != permission && "" != permission && !sender.hasPermission(permission)) {
				throw new IllegalArgumentException("You don't have permission for that!");
			}
			
			if(context.Args().length > depth) {
				CommandNode child = children.get(context.Args()[depth]);
				if(child != null) {
					return child.execute(context);
				}
				child = children.get(DYNAMIC_FILL);
				if(child != null) {
					return child.execute(context);
				}
			}

			return operation != null ? operation.apply(context) : noOperation(context);
		}
		
		private boolean noOperation(Context context) {
			String current = String.join(" ", context.Args()).trim();
			String message = "&7/" + context.Alias() + " " + current + "&c&o<--[HERE]";
			
			context.Sender().sendMessage(Text.format().colorize("&cUnknown command"));
			context.Sender().sendMessage(Text.format().colorize(message));
			return true;
		}
		
		boolean mergeFrom(CommandNode node) {
			if(operation != null && node.operation != null) {
				return false;
			}
			if(suggestions != null && node.suggestions != null) {
				return false;
			}
			
			Map<String, CommandNode> temp = new HashMap<>(children);
			
			for(Map.Entry<String, CommandNode> entry : node.children.entrySet()) {
				CommandNode child = temp.get(entry.getKey());
				if(child != null) {
					if(!child.mergeFrom(entry.getValue())) {
						return false;
					}
				}
				else {
					temp.put(entry.getKey(), entry.getValue());
				}
			}
			
			if(node.operation != null) {
				operation = node.operation;
			}
			if(node.suggestions != null) {
				suggestions = node.suggestions;
			}
			children = temp;
			return true;
		}
	}
	
	public class OperationBuilder {
		private CommandNode tree = new CommandNode(0);
		private CommandNode current = tree;
		
		private OperationBuilder() {
		}
		
		public OperationBuilder arg(String literal) {
			for(String s : literal.split(" ")) {
				if(s.length() > 0) {
					node(s);
				}
			}
			return this;
		}
		
		private void node(String literal) {
			CommandNode node = new CommandNode(current.depth + 1);
			
			for(String variant : literal.split("\\|")) {
				if(variant.length() > 0) {
					current.children.put(variant, node);
				}
			}
			
			current = node;
		}
		
		public OperationBuilder option(Function<Context, List<String>> daf) {
			CommandNode node = new CommandNode(current.depth + 1);
			current.suggestions = daf;
			
			current.children.put(DYNAMIC_FILL, node);
			current = node;
			
			return this;
		}
		
		public OperationBuilder requires(String permission) {
			current.permission = permission;
			return this;
		}
		
		public CommandBase calls(Function<Context,Boolean> callback) {
			current.operation = callback;
			root.mergeFrom(tree);
			
			return CommandBase.this;
		}
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
}