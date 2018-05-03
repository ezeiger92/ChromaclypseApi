package com.chromaclypse.api.messages.dynamic;

import java.util.List;

public class Fader extends DynamicText {
	
	public static final FaderDirection RIGHT_IN = FaderDirection.RIGHT_IN;
	public static final FaderDirection LEFT_IN = FaderDirection.LEFT_IN;
	public static final FaderDirection RIGHT_OUT = FaderDirection.RIGHT_OUT;
	public static final FaderDirection LEFT_OUT = FaderDirection.LEFT_OUT;
	
	private FaderDirection dir;

	private enum FaderDirection {
		LEFT_IN,
		RIGHT_IN,
		LEFT_OUT,
		RIGHT_OUT,
	}
	
	public Fader(List<String> text, FaderDirection dir) {
		super(text);
		this.dir = dir;
	}

	@Override
	protected String getStringAt(long currentMillis) {
		String result = textAsString();
		int L = result.length();
		int N = getIndex(currentMillis, 0, L);
		
		switch(dir) {
		case LEFT_IN: result = result.substring(0, N);
		case RIGHT_IN: result = result.substring(L-N, L);
		case LEFT_OUT: result = result.substring(0, L-N);
		case RIGHT_OUT: result = result.substring(N, L);
		}
		
		return result;
	}

}
