package com.chromaclypse.api.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.chromaclypse.api.messages.Text;

public class Dispatch implements CommandExecutor {
	@FunctionalInterface
	private static interface FunctionEx<T, R> {
		R apply(T object) throws Exception;
	}
	
	private final Map<String, FunctionEx<Context, Boolean>> callbacks = new HashMap<>();
	
	private static final Object tryInstance(Class<?> clazz) {
		try{
			return clazz.getConstructor().newInstance();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Dispatch(Object... args) {
		for(Object arg : args) {
			if(arg instanceof Class) {
				mapMethods(null, (Class<?>)arg);
			}
			else {
				mapMethods(arg, arg.getClass());
			}
		}
	}
	
	private static final boolean isCallback(Method m) {
		return m.getReturnType().equals(boolean.class) &&
				m.getParameterCount() == 1 &&
				m.getParameterTypes()[0].equals(Context.class);
	}
	
	private final void mapMethods(Object instance, Class<?> clazz) {
		for(Method m : clazz.getMethods()) {
			if(isCallback(m)) {
				if(Modifier.isStatic(m.getModifiers())) {
					callbacks.put(m.getName(), context -> (boolean)m.invoke(null, context));
				}
				else {
					final Object inst;
					if(instance == null) {
						if((instance = inst = tryInstance(clazz)) == null) {
							continue;
						}
					}
					else {
						inst = instance;
					}
					callbacks.put(m.getName(), context -> (boolean)m.invoke(inst, context));
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg3.length == 0) {
			return false;
		}
		
		FunctionEx<Context, Boolean> callback = callbacks.get(arg3[0]);
		
		if(callback != null) {
			try {
				return callback.apply(new Context(arg0, arg1, arg2, arg3));
			}
			catch(Exception e) {
				arg0.sendMessage(Text.format().colorize("&cError: " + e.getMessage()));
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
