package com.chromaclypse.api;

public class Chroma {
	private static Factory factory = null;
	
	public static Factory getFactory() {
		return factory;
	}
	
	public static Factory setFactory(Factory newFactory) {
		Factory old = factory;
		factory = newFactory;
		
		return old;
	}
}
