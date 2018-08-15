package com.chromaclypse.api.placeholder;

public abstract class Context {
	@SuppressWarnings("unchecked")
	public final <T extends Context> T asInterface() {
		return (T)this;
	}
	
	public final <T extends Context> T asInterface(Class<T> clazz) {
		return asInterface();
	}
}
