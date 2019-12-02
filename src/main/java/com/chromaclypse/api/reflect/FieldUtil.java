package com.chromaclypse.api.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldUtil {
	private static final Field modifiers;
	
	static {
		Field mod = null;
		try {
			mod = Field.class.getDeclaredField("modifiers");
		}
		catch(Exception e) {
		}
		
		modifiers = mod;
	}

	public static Object get(Object owner, Field field) {
		boolean wasAccessible = field.isAccessible();
		field.setAccessible(true);
		
		Object value = null;
		
		try {
			value = field.get(owner);
		} catch (Exception e) {
		}
		
		field.setAccessible(wasAccessible);
		
		return value;
	}
	
	public static void set(Object owner, Field field, Object value) {
		boolean wasAccessible = field.isAccessible();
		field.setAccessible(true);
		
		int finalBit = field.getModifiers() & Modifier.FINAL;
		
		if(finalBit != 0) {
			modifiers.setAccessible(true);
			
			try {
				modifiers.setInt(field, field.getModifiers() & ~finalBit);
			}
			catch(Exception e) {
			}
		}
		
		try {
			field.set(owner, value);
		}
		catch(Exception e) {
		}
		
		if(finalBit != 0) {
			try {
				modifiers.setInt(field, field.getModifiers() | finalBit);
			}
			catch(Exception e) {
			}
			
			modifiers.setAccessible(false);
		}
		
		field.setAccessible(wasAccessible);
	}
}
