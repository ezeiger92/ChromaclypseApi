package com.chromaclypse.api;

import java.util.function.Supplier;

public interface Factory {
	<T> T construct(Class<T> clazz);
	
	@Deprecated
	<T> T instance(Class<T> clazz);
	
	<T> void register(Class<T> clazz, Class<? extends T> implementation);
	
	<T> void registerSupplier(Class<T> clazz, Supplier<? extends T> func);

	<T> void registerSingleton(Class<T> abstractClass, Supplier<? extends T> func);
}
