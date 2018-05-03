package com.chromaclypse.api;

public interface Factory {
	public <T> T construct(Class<T> clazz);
	public <T> T instance(Class<T> clazz);
}
