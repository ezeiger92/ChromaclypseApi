package com.chromaclypse.api.messages.dynamic;

import java.util.List;

public abstract class DynamicText {
	protected List<String> text;
	protected long updateMillis;
	
	protected DynamicText(List<String> text) {
		this(text, 1000L);
	}
	
	protected DynamicText(List<String> text, long updateMillis) {
		this.text = text;
		this.updateMillis = updateMillis;
	}
	
	protected int getIndex(long currentMillis, int min, int max) {
		
		long index = Math.floorDiv(currentMillis, updateMillis);
		index = Math.floorMod(index, max - min);
		
		return (int)index + min;
	}
	
	protected String textAsString() {
		if(text.size() == 0)
			return "";
		
		StringBuilder sb = new StringBuilder();
		for(String s : text)
			sb.append(s).append('\n');
		
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
	
	@Override
	public String toString() {
		return getStringAt(System.currentTimeMillis());
	}
	
	protected abstract String getStringAt(long currentMillis);
}
