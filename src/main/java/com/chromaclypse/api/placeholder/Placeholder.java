package com.chromaclypse.api.placeholder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.entity.Player;

public class Placeholder {
	Map<String, Fragment> placeholderMap = new HashMap<>();
	Map<String, Function<Context, Object>> phm = new HashMap<>();

	public static class PlayerContext extends Context {
		private Player object;
		public PlayerContext(Player object) {
			this.object = object;
		}
		
		public Player getPlayer() {
			return object;
		}
	}
	
	public static class GenericContext<T> extends Context {
		private T object;
		public GenericContext(T object) {
			this.object = object;
		}
		
		public T get() {
			return object;
		}
	}
	
	public Object eval(String placeholder, Context context) {
		Fragment fg = placeholderMap.get(placeholder);
		
		if(fg != null)
			return fg.execute(context);
		
		return null;
	}
	
	public Placeholder() {
		placeholderMap.put("player", new Fragment() {
			public Object onExecute(Context context) {
				PlayerContext pc = context.asInterface();
				return pc.getPlayer().getName();
			}
		});
		
		phm.put("player", c -> c.asInterface(PlayerContext.class).getPlayer().getName());
		
		placeholderMap.put("test", new Fragment() {

			@Override
			public Object onExecute(Context context) {
				GenericContext<Player> c = context.asInterface();
				return c.get().getName();
			}
			
		});
	}
}
