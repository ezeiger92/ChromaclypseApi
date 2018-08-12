package com.chromaclypse.api.messages;

import java.util.List;

import com.chromaclypse.api.Chroma;

public class Text {
	private static Formatter formatter = Chroma.get().factory().construct(Formatter.class);
	
	public static Formatter format() {
		return formatter;
	}
	
	@Deprecated
	public static String colorize(String input) {
		return formatter.colorize(input);
	}

	@Deprecated
	public static String colorize(String... inputs) {
		return formatter.colorize(inputs);
	}

	@Deprecated
	public static String[] colorizeList(String... inputs) {
		return formatter.colorizeList(inputs);
	}

	@Deprecated
	public static String decolor(String input) {
		return formatter.decolor(input);
	}

	@Deprecated
	public static String decolor(String... inputs) {
		return formatter.decolor(inputs);
	}

	@Deprecated
	public static String[] decolorList(String... inputs) {
		return formatter.decolorList(inputs);
	}

	@Deprecated
	public static String niceName(String input) {
		return formatter.niceName(input);
	}

	@Deprecated
	public static List<String> wrap(int max_length, String... input) {
		return formatter.wrap(max_length, input);
	}
}
