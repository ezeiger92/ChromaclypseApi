package com.chromaclypse.api.json;

import java.util.Map;

public class DefaultProp implements Prop {
	private final String key;
	private final String value;

	public DefaultProp(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void apply(Map<String, String> properties) {
		properties.put(key, value);
	}
}
