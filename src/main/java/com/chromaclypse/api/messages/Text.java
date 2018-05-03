package com.chromaclypse.api.messages;

import java.util.List;

import com.chromaclypse.api.Chroma;

public class Text {
	private static Formatter formatter = Chroma.getFactory().construct(Formatter.class);
	
	public static String colorize(String input) {
		return formatter.colorize(input);
	}

	public static String colorize(String... inputs) {
		return formatter.colorize(inputs);
	}
	
	public static String[] colorizeList(String... inputs) {
		return formatter.colorizeList(inputs);
	}
	
	public static String decolor(String input) {
		return formatter.decolor(input);
	}

	public static String decolor(String... inputs) {
		return formatter.decolor(inputs);
	}
	
	public static String[] decolorList(String... inputs) {
		return formatter.decolorList(inputs);
	}
	
	public static String niceName(String input) {
		return formatter.niceName(input);
	}
	
	public static List<String> wrap(int max_length, String... input) {
		return formatter.wrap(max_length, input);
	}
}
