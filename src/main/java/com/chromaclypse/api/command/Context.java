package com.chromaclypse.api.command;

import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Context {
	private final CommandSender sender;
	private final Command command;
	private final String alias;
	private final String[] args;
	
	public Context(CommandSender sender, Command command, String alias, String[] args) {
		this.sender = sender;
		this.command = command;
		this.alias = alias;
		this.args = args;
	}
	
	public CommandSender Sender() {
		return sender;
	}
	
	public Command Command() {
		return command;
	}
	
	public String Alias() {
		return alias;
	}
	
	public String[] Args() {
		return args;
	}
	
	public Player Player() {
		if(sender instanceof Player) {
			return (Player) sender;
		}
		
		throw new ClassCastException("Expected sender to be a Player");
	}
	
	public String GetArg(int index) {
		if(index >= 0 && index < args.length) {
			return args[index];
		}
		
		throw new IndexOutOfBoundsException("Expected index in the range [0.." + (args.length - 1) + "], but got " + index);
	}
	
	public <T> T GetArg(int index, Function<String, T> op) {
		return GetArg(index, op, "expected type");
	}
	
	public <T> T GetArg(int index, Function<String, T> op, String typeName) {
		String temp = GetArg(index);
		try {
			return op.apply(temp);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("Could not convert string " + temp + " to " + typeName, e);
		}
	}
	
	public String SplatArgs(int from) {
		return SplatArgs(from, args.length - 1);
	}
	
	public String SplatArgs(int from, int to) {
		from = Math.max(from, 0);
		to = Math.min(to, args.length -1);
		if(from <= to) {
			StringBuilder res = new StringBuilder(args[from]);
			
			for(int i = from + 1; i <= to; ++i) {
				res.append(' ').append(args[i]);
			}
			
			return res.toString();
		}
		
		return "";
	}
	
	public Material GetMaterial(int index) {
		String arg = GetArg(index);
		Material mat = Material.matchMaterial(arg);
		
		if(mat != null) {
			return mat;
		}
		
		throw new IllegalArgumentException("Expected material name, but got \"" + arg + "\"");
	}
	
	public ItemStack GetHeld() {
		ItemStack hand = Player().getInventory().getItemInMainHand();
		
		if(hand.getType() != Material.AIR) {
			return hand;
		}
		
		throw new IllegalStateException("Expected player to be holding an item");
	}
}
