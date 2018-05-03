package com.chromaclypse.api.placeholder;

public class Context {
	@SuppressWarnings("unchecked")
	public <T extends Context> T getInterface() {
		return (T)this;
	}
}
