package com.chromaclypse.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @brief     Initialize Maps and Lists.
 * @author    Erik Zeiger
 * @version   1.1
 * @date      2017-
 * @warning   Keys() needs either arguments or an explicit type when chained with Values().
 * @copyright MIT License
 *
 */
public class Defaults {

	@SafeVarargs
	public static <T> List<T> List(T... args) {
		return new ArrayList<>(Arrays.asList(args));
	}
	
	@SafeVarargs
	public static <K> KeySet<K> Keys(K... args) {
		return new KeySet<>(args);
	}
	
	public static <T> List<T> EmptyList() {
		return new ArrayList<>();
	}
	
	public static <K, V> Map<K, V> EmptyMap() {
		return new HashMap<>();
	}
	
	public static <T> Set<T> EmptySet() {
		return new HashSet<>();
	}
	
	public static class Pair<K,V> {
		K val1;
		V val2;
		
		public Pair(K key, V val) {
			val1 = key;
			val2 = val;
		}
		
		public K getKey() {
			return val1;
		}
		
		public V getVal() {
			return val2;
		}
		
		public void setKey(K newKey) {
			val1 = newKey;
		}
		
		public void setVal(V newVal) {
			val2 = newVal;
		}
		
		@Override
		public String toString() {
			return "[" + val1.toString() + ", " + val2.toString() + "]";
		}
		
		@Override
		public boolean equals(Object left) {
			return hashCode() == left.hashCode();
		}
		
		@Override
		public int hashCode() {
			return toString().hashCode();
		}
	}
	
	public static class KeySet<K> {
		K[] keys;
		
		@SafeVarargs
		public KeySet(K... args) {
			keys = args;
		}
		
		@SafeVarargs
		public final <V> Map<K,V> Values(V... args) {
			Map<K,V> result = new HashMap<>();
			
			if(keys.length != args.length) {
				// Length mismatch warning
			}
			
			for(int i = 0; i < keys.length; ++i)
				if(i < args.length)
					result.put(keys[i], args[i]);
				else
					result.put(keys[i], null);
			
			return result;
		}
	}
}
