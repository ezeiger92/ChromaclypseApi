package com.chromaclypse.api.messages.dynamic;

import java.util.List;

public class Cycler extends DynamicText {

	public Cycler(List<String> text) {
		super(text);
	}

	@Override
	protected String getStringAt(long currentMillis) {
		int index = getIndex(currentMillis, 0, text.size());
		
		return text.get(index);
	}
}
