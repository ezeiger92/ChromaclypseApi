package com.chromaclypse.api.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

class CommandNode {
	private static final String DYNAMIC_FILL = "<dynamic>";
	private final int depth;
	private Map<String, CommandNode> children = new HashMap<>();
	private Function<Context, String> permissionSource = CommandNode::defaultPermission;
	private Function<Context, Boolean> operation = null;
	private Function<Context, List<String>> suggestions = null;
	
	private static final String defaultPermission(Context context) {
		return "*";
	}
	
	CommandNode() {
		this.depth = 0;
	}
	
	private CommandNode(int depth) {
		this.depth = depth;
	}
	
	void setPermission(Function<Context, String> permissionSource) {
		this.permissionSource = permissionSource;
	}
	
	void setOperation(Function<Context, Boolean> operation) {
		this.operation = operation;
	}
	
	void setSuggestions(Function<Context, List<String>> suggestions) {
		this.suggestions = suggestions;
	}
	
	CommandNode makeChild(String... keys) {
		CommandNode child = new CommandNode(depth + 1);
		for(String s : keys) {
			if(s.length() > 0) {
				children.put(s, child);
			}
		}
		
		return child;
	}
	
	CommandNode makeWildcardChild() {
		return makeChild(DYNAMIC_FILL);
	}
	
	List<String> tabCompletion(Context context) {
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
				.filter(e -> sender.hasPermission(e.getValue().permissionSource.apply(context)))
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
	
	boolean execute(Context context) {
		CommandSender sender = context.Sender();
		String permission = permissionSource.apply(context);
		
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

		return operation != null ? operation.apply(context) : CommandBase.unknownCommand(context);
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
