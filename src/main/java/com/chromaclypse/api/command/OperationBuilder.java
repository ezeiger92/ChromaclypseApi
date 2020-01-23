package com.chromaclypse.api.command;

import java.util.List;
import java.util.function.Function;

public class OperationBuilder {
	private final CommandBase command;
	private final CommandNode tree = new CommandNode();
	private CommandNode current = tree;
	
	OperationBuilder(CommandBase command) {
		this.command = command;
	}
	
	public OperationBuilder arg(String literal) {
		for(String s : literal.split(" ")) {
			if(s.length() > 0) {
				current = current.makeChild(literal.split("\\|"));
			}
		}
		return this;
	}
	
	public OperationBuilder option(Function<Context, List<String>> values) {
		current.setSuggestions(values);
		current = current.makeWildcardChild();
		
		return this;
	}
	
	public OperationBuilder requires(String permission) {
		current.setPermission(c -> permission);
		return this;
	}
	
	public OperationBuilder requires(Function<Context, String> permissionSource) {
		current.setPermission(permissionSource);
		return this;
	}
	
	public CommandBase calls(Function<Context,Boolean> callback) {
		current.setOperation(callback);
		command.getRoot().mergeFrom(tree);
		
		return command;
	}
}
