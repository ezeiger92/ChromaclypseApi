package com.chromaclypse.api.json;

import java.util.Map;

public class DefaultProp implements Prop {
	private String key;
	private String value;

	public DefaultProp(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void apply(Map<String, String> properties) {
		properties.put(key, value);
	}
}
