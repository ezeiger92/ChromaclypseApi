package com.chromaclypse.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @brief     Initialize Maps and Lists.
 * @author    Erik Zeiger
 * @version   1.1
 * @date      2017-
 * @warning   keys() needs either arguments or an explicit type when chained with values().
 * @copyright MIT License
 *
 */
public class Defaults {

	@SafeVarargs
	public static <T> List<T> list(T e, T... extra) {
		ArrayList<T> result = new ArrayList<>(extra.length + 1);
		result.add(e);
		result.addAll(Arrays.asList(extra));
		
		return result;
	}

	@Deprecated
	@SafeVarargs
	public static <T> List<T> List(T... args) {
		if(args.length > 0) {
			return new ArrayList<>(Arrays.asList(args));
		}
		
		return emptyList();
	}
	
	@SafeVarargs
	public static <K> KeySet<K> keys(K... args) {
		return new KeySet<>(args);
	}

	@Deprecated
	@SafeVarargs
	public static <K> KeySet<K> Keys(K... args) {
		return keys(args);
	}
	
	public static <T> List<T> emptyList() {
		return new ArrayList<>();
	}
	
	@Deprecated
	public static <T> List<T> EmptyList() {
		return emptyList();
	}
	
	public static <K, V> Map<K, V> emptyMap() {
		return new HashMap<>();
	}

	@Deprecated
	public static <K, V> Map<K, V> EmptyMap() {
		return emptyMap();
	}
	
	public static <T> Set<T> emptySet() {
		return new HashSet<>();
	}

	@Deprecated
	public static <T> Set<T> EmptySet() {
		return emptySet();
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
			if(left instanceof Pair) {
				Pair<?,?> that = (Pair<?,?>) left;
				
				return
					Objects.equals(val1, that.val1) &&
					Objects.equals(val2, that.val2);
			}
			
			return false;
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
		public final <V> Map<K,V> values(V... args) {
			Map<K,V> result = emptyMap();
			
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
		
		@Deprecated
		@SafeVarargs
		public final <V> Map<K,V> Values(V... args) {
			return values(args);
		}
	}
}
