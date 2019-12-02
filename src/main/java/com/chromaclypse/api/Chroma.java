package com.chromaclypse.api;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public interface Chroma {
	
	Factory factory();
	Plugin plugin();
	Logger log();
	
	static Chroma get() {
		return Impl.chroma;
	}
	
	@Deprecated
	abstract class Impl implements Chroma {
		private static Chroma chroma = null;
		
		protected Impl() throws IllegalStateException {
			if(chroma != null) {
				throw new IllegalStateException("Chroma singleton already constructed");
			}
			
			chroma = this;
		}
	}
}
