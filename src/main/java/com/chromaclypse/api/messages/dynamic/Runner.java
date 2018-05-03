package com.chromaclypse.api.messages.dynamic;

import java.util.List;

public class Runner extends DynamicText {

	protected Runner(List<String> text) {
		super(text);
	}
	
	

	@Override
	protected String getStringAt(long currentMillis) {
		return textAsString();
	}

}
