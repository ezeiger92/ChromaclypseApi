package com.chromaclypse.api.config.options;

public @interface ConfigOptions {
	Access level() default Access.PUBLIC;
	
	Class<?> ctor() default void.class;
}
